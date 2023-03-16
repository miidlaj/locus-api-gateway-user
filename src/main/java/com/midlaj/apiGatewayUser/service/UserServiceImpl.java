package com.midlaj.apiGatewayUser.service;

import com.midlaj.apiGatewayUser.dto.request.EmailDTO;
import com.midlaj.apiGatewayUser.dto.request.PasswordResetDTO;
import com.midlaj.apiGatewayUser.model.AuthProvider;
import com.midlaj.apiGatewayUser.model.Role;
import com.midlaj.apiGatewayUser.model.User;
import com.midlaj.apiGatewayUser.payload.SignUpRequest;
import com.midlaj.apiGatewayUser.repository.RoleRepository;
import com.midlaj.apiGatewayUser.repository.UserRepository;
import com.midlaj.apiGatewayUser.utils.AESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static Long NORMAL_USER_ID = Long.valueOf(1);

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<? extends Object> saveUser(SignUpRequest signUpRequest) throws URISyntaxException {

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.NOT_ACCEPTABLE);
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPhone(signUpRequest.getPhone());
        user.setProvider(AuthProvider.local);
        user.setCreatedTime(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findById(1L).get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);

        user.setRoles(userRoles);

        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        User result = userRepository.save(user);
        System.out.println(result.getVerificationCode());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();


        String verificationLink = buildVerificationLink(verificationCode);


        ResponseEntity response = emailService.sendEmail(EmailDTO.builder()
                    .to(user.getEmail())
                    .subject("Verify your Account")
                    .body(buildEmailBodyForVerification(verificationLink))
                    .build());

        String message = "User registered successfully. Check your registered email for activating your account.";
        if (response.getStatusCode() != HttpStatus.OK) {
            message = "Account created successfully. But cannot verify your account at the moment. Please try after some time.";
        }

        return ResponseEntity.created(location).body(message);
    }

    @Override
    public ResponseEntity<?> verifyUser(String verificationCode) {
        System.out.println(verificationCode);
        User user = userRepository.findUserByVerificationCode(verificationCode);

        if (user == null) {
            log.warn("Cannot find user with verification Code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification code is Invalid");
        }

        String verificationCodeInDb = user.getVerificationCode();
        boolean isMatch = verificationCodeInDb.equals(verificationCode);
        System.out.println("Verification code in db " + verificationCodeInDb);
        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Verification code is Invalid");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);

        userRepository.save(user);
        return ResponseEntity.ok("User verified Successfully.");
    }

    @Override
    public ResponseEntity<?> handleForgetPassword(String email) throws URISyntaxException {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find an user with email " + email);
        }

        User user = userOptional.get();
        Date date = new Date();
        String resetCode = AESService.encrypt(date.toString());

        String passwordResetLink = buildPasswordResetLink(resetCode);
        String emailBody = buildEmailBodyForPasswordReset(passwordResetLink);

        ResponseEntity<?> response = emailService.sendEmail(EmailDTO.builder()
                .to(user.getEmail())
                .subject("Reset you Password")
                .body(emailBody)
                .build());

        String message = "Kindly check your email for changing your password.";
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error(response.getBody().toString());
            message = "Cannot reset the password at the moment.";
        }

        user.setResetPasswordToken(resetCode);
        userRepository.save(user);


        return ResponseEntity.ok(message);
    }

    @Override
    public ResponseEntity<?> changePasswordWithResetCode(PasswordResetDTO passwordResetDTO) {
        User user = userRepository.findUserByResetPasswordToken(passwordResetDTO.getResetPasswordToken());

        if (user == null) {
            log.warn("Cannot find user with verification Code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password Reset code is Invalid");
        }

        String resetPasswordTokenInDb = user.getResetPasswordToken();
        boolean isMatch = resetPasswordTokenInDb.equals(passwordResetDTO.getResetPasswordToken());
        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password Reset code is Invalid");
        }

        String decryptedCode = AESService.decrypt(passwordResetDTO.getResetPasswordToken());
        Date decryptedAsDate;
        try {
            decryptedAsDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(decryptedCode);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password Reset code is Invalid");
        }

        Date currentDate = new Date();
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);
        currentCal.add(Calendar.MINUTE, -10);
        Date tenMinutesAgo = currentCal.getTime();

        boolean isValid = false;
        if (decryptedAsDate.after(tenMinutesAgo)) {
            isValid = true;
        }

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.GONE).body("Password reset link has expired. Please try again");
        }

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }

    private String buildPasswordResetLink(String passwordResetcode) {
        return "http://localhost:3000/forget?token=" + passwordResetcode;
    }

    private String buildEmailBodyForPasswordReset(String passwordResetLink) {

        String emailBody = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <style type=\"text/css\">\n" +
                "     @media screen {\n" +
                "            @font-face {\n" +
                "                font-family: 'Montserrat';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Montserrat'), local('Montserrat'), url(https://fonts.google.com/share?selection.family=Montserrat:ital,wght@1,200);\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Montserrat';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Montserrat Bold'), local('Montserrat-Bold'), url(https://fonts.google.com/share?selection.family=Montserrat%20Subrayada:wght@700%7CMontserrat:ital,wght@1,200);\n" +
                "            }\n" +
                "\n" +
                "            /* CLIENT-SPECIFIC STYLES */\n" +
                "            body,\n" +
                "            table,\n" +
                "            td,\n" +
                "            a {\n" +
                "                -webkit-text-size-adjust: 100%;\n" +
                "                -ms-text-size-adjust: 100%;\n" +
                "            }\n" +
                "\n" +
                "            table,\n" +
                "            td {\n" +
                "                mso-table-lspace: 0pt;\n" +
                "                mso-table-rspace: 0pt;\n" +
                "            }\n" +
                "\n" +
                "            img {\n" +
                "                -ms-interpolation-mode: bicubic;\n" +
                "            }\n" +
                "\n" +
                "            /* RESET STYLES */\n" +
                "            img {\n" +
                "                border: 0;\n" +
                "                height: auto;\n" +
                "                line-height: 100%;\n" +
                "                outline: none;\n" +
                "                text-decoration: none;\n" +
                "            }\n" +
                "\n" +
                "            table {\n" +
                "                border-collapse: collapse !important;\n" +
                "            }\n" +
                "\n" +
                "            body {\n" +
                "                height: 100% !important;\n" +
                "                margin: 0 !important;\n" +
                "                padding: 0 !important;\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            /* iOS BLUE LINKS */\n" +
                "            a[x-apple-data-detectors] {\n" +
                "                color: inherit !important;\n" +
                "                text-decoration: none !important;\n" +
                "                font-size: inherit !important;\n" +
                "                font-family: inherit !important;\n" +
                "                font-weight: inherit !important;\n" +
                "                line-height: inherit !important;\n" +
                "            }\n" +
                "\n" +
                "            /* MOBILE STYLES */\n" +
                "            @media screen and (max-width:600px) {\n" +
                "                h1 {\n" +
                "                    font-size: 32px !important;\n" +
                "                    line-height: 32px !important;\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            /* ANDROID CENTER FIX */\n" +
                "            div[style*=\"margin: 16px 0;\"] {\n" +
                "                margin: 0 !important;\n" +
                "            }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #f4f4f4; margin: 0 !important; padding: 0 !important;\">\n" +
                "    </head> <!-- HIDDEN PREHEADER TEXT -->\n" +
                "    <div\n" +
                "        style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Montserrat'Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\n" +
                "        Go anywhere with anywheel!</div>\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <!-- LOGO -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\"\n" +
                "                            style=\"padding: 40px 20px 20px 20px; border-radius: 2px 2px 0px 0px; color: #014737; font-family: 'Londrina Solid'Helvetica, Arial, sans-serif; font-size: 45px; font-weight: 700; letter-spacing: 2px; line-height: 48px;\">            <img src=\"https://i.ibb.co/25SV90T/oie-f-CAaxhcl-GNkh.png\" alt=\"Logo\"/>\n" +
                "                            <h1 class=\"logo-text\" style=\"font-size: 40px; font-weight:700; margin: w-50;\">LOCUS HAUNT</h1>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 20px 30px 40px 30px; color: #000000; font-size: 16px; font-weight:900; line-height: 25px;\">\n" +
                "                            <p>You have requested to reset the password. Kindly click the link below for changing the password.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\">\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n" +
                "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                                <td align=\"center\" style=\"border-radius: 30px;\" bgcolor=\"#000000\"><a\n" +
                "                                                        href=\"[[passwordResetLink]]\" target=\"_blank\"\n" +
                "                                                        style=\"font-size: 20px; font-family: 'Montserrat Bold'Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 10px 55px; border-radius: 2px; display: inline-block;\">RESET HERE</a></td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr> <!-- COPY -->\n" +
                "                    \n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 20px 30px; color: #FF0000; font-family:'Montserrat'Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">The link will expires in 10 minutes.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #000000; font-family:'Montserrat'Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">Contact us at <a href=\"mailto:locushaunt@gmail.com\" target=\"_blank\"\n" +
                "                                    style=\"color: #29ABE2;\">locushaunt@gmail.com</a></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #333333; font-family:'Montserrat'Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <img src=\"https://img.icons8.com/ios-glyphs/30/000000/facebook-new.png\"/> <img src=\"https://img.icons8.com/material-outlined/30/000000/instagram-new.png\"/>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#f4f4f4\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 30px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 18px;\">\n" +
                "                            <br>\n" +
                "                            <p style=\"margin: ;\"><a href=\"#\" target=\"_blank\" style=\"color: #111111; font-weight: 700;\"\n" +
                "                                    </p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";

        emailBody = emailBody.replace("[[passwordResetLink]]", passwordResetLink);

        return emailBody;
    }

    private String buildVerificationLink(String verificationCode) {
        return "http://localhost:3000/verify?token=" + verificationCode;
    }

    private String buildEmailBodyForVerification(String verificationLink) {

        String emailBody = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <style type=\"text/css\">\n" +
                "     @media screen {\n" +
                "            @font-face {\n" +
                "                font-family: 'Montserrat';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Montserrat'), local('Montserrat'), url(https://fonts.google.com/share?selection.family=Montserrat:ital,wght@1,200);\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Montserrat';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Montserrat Bold'), local('Montserrat-Bold'), url(https://fonts.google.com/share?selection.family=Montserrat%20Subrayada:wght@700%7CMontserrat:ital,wght@1,200);\n" +
                "            }\n" +
                "\n" +
                "            /* CLIENT-SPECIFIC STYLES */\n" +
                "            body,\n" +
                "            table,\n" +
                "            td,\n" +
                "            a {\n" +
                "                -webkit-text-size-adjust: 100%;\n" +
                "                -ms-text-size-adjust: 100%;\n" +
                "            }\n" +
                "\n" +
                "            table,\n" +
                "            td {\n" +
                "                mso-table-lspace: 0pt;\n" +
                "                mso-table-rspace: 0pt;\n" +
                "            }\n" +
                "\n" +
                "            img {\n" +
                "                -ms-interpolation-mode: bicubic;\n" +
                "            }\n" +
                "\n" +
                "            /* RESET STYLES */\n" +
                "            img {\n" +
                "                border: 0;\n" +
                "                height: auto;\n" +
                "                line-height: 100%;\n" +
                "                outline: none;\n" +
                "                text-decoration: none;\n" +
                "            }\n" +
                "\n" +
                "            table {\n" +
                "                border-collapse: collapse !important;\n" +
                "            }\n" +
                "\n" +
                "            body {\n" +
                "                height: 100% !important;\n" +
                "                margin: 0 !important;\n" +
                "                padding: 0 !important;\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            /* iOS BLUE LINKS */\n" +
                "            a[x-apple-data-detectors] {\n" +
                "                color: inherit !important;\n" +
                "                text-decoration: none !important;\n" +
                "                font-size: inherit !important;\n" +
                "                font-family: inherit !important;\n" +
                "                font-weight: inherit !important;\n" +
                "                line-height: inherit !important;\n" +
                "            }\n" +
                "\n" +
                "            /* MOBILE STYLES */\n" +
                "            @media screen and (max-width:600px) {\n" +
                "                h1 {\n" +
                "                    font-size: 32px !important;\n" +
                "                    line-height: 32px !important;\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            /* ANDROID CENTER FIX */\n" +
                "            div[style*=\"margin: 16px 0;\"] {\n" +
                "                margin: 0 !important;\n" +
                "            }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #f4f4f4; margin: 0 !important; padding: 0 !important;\">\n" +
                "    </head> <!-- HIDDEN PREHEADER TEXT -->\n" +
                "    <div\n" +
                "        style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Montserrat'Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\n" +
                "        Go anywhere with anywheel!</div>\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <!-- LOGO -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\"\n" +
                "                            style=\"padding: 40px 20px 20px 20px; border-radius: 2px 2px 0px 0px; color: #014737; font-family: 'Londrina Solid'Helvetica, Arial, sans-serif; font-size: 45px; font-weight: 700; letter-spacing: 2px; line-height: 48px;\">            <img src=\"https://i.ibb.co/25SV90T/oie-f-CAaxhcl-GNkh.png\" alt=\"Logo\"/>\n" +
                "                            <h1 class=\"logo-text\" style=\"font-size: 40px; font-weight:700; margin: w-50;\">LOCUS HAUNT</h1>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 20px 30px 40px 30px; color: #000000; font-size: 16px; font-weight:900; line-height: 25px;\">\n" +
                "                            <p>Kindly verify your email to complete your account registration.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\">\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n" +
                "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                                <td align=\"center\" style=\"border-radius: 30px;\" bgcolor=\"#000000\"><a\n" +
                "                                                        href=\"[[verificationLink]]\" target=\"_blank\"\n" +
                "                                                        style=\"font-size: 20px; font-family: 'Montserrat Bold'Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 10px 55px; border-radius: 2px; display: inline-block;\">VERIFY HERE</a></td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr> <!-- COPY -->\n" +
                "                    \n" +
                "                   \n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #000000; font-family:'Montserrat'Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">Contact us at <a href=\"mailto:locushaunt@gmail.com\" target=\"_blank\"\n" +
                "                                    style=\"color: #29ABE2;\">locushaunt@gmail.com</a></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #333333; font-family:'Montserrat'Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <img src=\"https://img.icons8.com/ios-glyphs/30/000000/facebook-new.png\"/> <img src=\"https://img.icons8.com/material-outlined/30/000000/instagram-new.png\"/>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#f4f4f4\" align=\"center\"\n" +
                "                            style=\"padding: 0px 30px 30px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 18px;\">\n" +
                "                            <br>\n" +
                "                            <p style=\"margin: ;\"><a href=\"#\" target=\"_blank\" style=\"color: #111111; font-weight: 700;\"\n" +
                "                                    </p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";

        emailBody = emailBody.replace("[[verificationLink]]", verificationLink);

        return emailBody;
    }
}

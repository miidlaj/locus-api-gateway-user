package com.midlaj.apiGatewayUser.controller;

import com.midlaj.apiGatewayUser.dto.request.PasswordResetDTO;
import com.midlaj.apiGatewayUser.payload.LoginRequest;
import com.midlaj.apiGatewayUser.payload.SignUpRequest;
import com.midlaj.apiGatewayUser.service.AuthenticationService;
import com.midlaj.apiGatewayUser.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

       return authenticationService.signInAndReturnJWT(loginRequest);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws URISyntaxException {

        return userService.saveUser(signUpRequest);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@Param("verificationCode") String verificationCode) {
        log.info("Inside the verifyUser method in Auth Controller");

        return userService.verifyUser(verificationCode);
    }

    @GetMapping("/forget")
    public ResponseEntity<?> forgetPassword(@Param("email") String email) throws URISyntaxException {
        log.info("Inside the forgetPassword method in Auth Controller");

        return userService.handleForgetPassword(email);
    }

    @PostMapping("/forget")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        log.info("Inside the resetPassword method in Auth Controller");

        return userService.changePasswordWithResetCode(passwordResetDTO);
    }
}

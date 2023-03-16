package com.midlaj.apiGatewayUser.service;

import com.midlaj.apiGatewayUser.dto.request.PasswordResetDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface UserService {

    ResponseEntity<? extends Object> saveUser(com.midlaj.apiGatewayUser.payload.SignUpRequest signUpRequest) throws URISyntaxException;

    ResponseEntity<?> verifyUser(String verificationCode);

    ResponseEntity<?> handleForgetPassword(String email) throws URISyntaxException;

    ResponseEntity<?> changePasswordWithResetCode(PasswordResetDTO passwordResetDTO);

}

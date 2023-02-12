package com.midlaj.apiGatewayUser.service;

import com.midlaj.apiGatewayUser.payload.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> signInAndReturnJWT(LoginRequest loginRequest);
}

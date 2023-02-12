package com.midlaj.apiGatewayUser.service;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<? extends Object> saveUser(com.midlaj.apiGatewayUser.payload.SignUpRequest signUpRequest);

//    Optional<User> findByUsername(String username);
}

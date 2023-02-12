//package com.midlaj.apiGatewayUser.controller;
//
//import com.midlaj.apiGatewayUser.requestDTO.SignInRequest;
//import com.midlaj.apiGatewayUser.requestDTO.SignUpRequest;
//import com.midlaj.apiGatewayUser.responseDTO.SignInResponse;
//import com.midlaj.apiGatewayUser.service.AuthenticationService;
//import com.midlaj.apiGatewayUser.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@Slf4j
//public class AuthenticationController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthenticationService authenticationService;
//
//    @PostMapping("register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
//        log.info("Inside the signUp method in AuthController");
//
//        if (userService.findByUsername(signUpRequest.getUsername()).isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        return new ResponseEntity<>(userService.saveUser(signUpRequest), HttpStatus.CREATED);
//    }
//
//    @PostMapping("login")
//    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
//        log.info("Inside the signIn method in AuthController");
//
//        SignInResponse response = authenticationService.signInAndReturnJWT(signInRequest);
//
//        if (response == null) return new ResponseEntity<>(HttpStatus.LOCKED);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//
//
//}

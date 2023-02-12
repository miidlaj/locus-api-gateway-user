package com.midlaj.apiGatewayUser.controller;

import com.midlaj.apiGatewayUser.exception.BadRequestException;
import com.midlaj.apiGatewayUser.model.AuthProvider;
import com.midlaj.apiGatewayUser.model.Role;
import com.midlaj.apiGatewayUser.model.User;
import com.midlaj.apiGatewayUser.payload.ApiResponse;
import com.midlaj.apiGatewayUser.payload.AuthResponse;
import com.midlaj.apiGatewayUser.payload.LoginRequest;
import com.midlaj.apiGatewayUser.payload.SignUpRequest;
import com.midlaj.apiGatewayUser.repository.RoleRepository;
import com.midlaj.apiGatewayUser.repository.UserRepository;
import com.midlaj.apiGatewayUser.security.TokenProvider;
import com.midlaj.apiGatewayUser.security.UserPrincipal;
import com.midlaj.apiGatewayUser.service.AuthenticationService;
import com.midlaj.apiGatewayUser.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        return userService.saveUser(signUpRequest);
    }
}

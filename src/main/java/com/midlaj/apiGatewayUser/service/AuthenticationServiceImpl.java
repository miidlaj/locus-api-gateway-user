package com.midlaj.apiGatewayUser.service;

import com.midlaj.apiGatewayUser.payload.AuthResponse;
import com.midlaj.apiGatewayUser.payload.LoginRequest;
import com.midlaj.apiGatewayUser.security.TokenProvider;
import com.midlaj.apiGatewayUser.security.UserPrincipal;
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
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;



    @Override
    public ResponseEntity<?> signInAndReturnJWT(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (DisabledException e) {
            log.error("User is disabled");
            return new ResponseEntity<>("User is disabled", HttpStatus.NOT_ACCEPTABLE);
        } catch (BadCredentialsException e) {
            log.error("Bad credentials");
            return new ResponseEntity<>("Bad Credentials", HttpStatus.LOCKED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = tokenProvider.createToken(authentication);


        return ResponseEntity.ok(AuthResponse.builder()
                .email(userPrincipal.getUsername())
                .name(userPrincipal.getName())
                .phone(userPrincipal.getPhone())
                .jwtToken(jwt)
                .id(String.valueOf(userPrincipal.getId()))
                .build());
    }
}

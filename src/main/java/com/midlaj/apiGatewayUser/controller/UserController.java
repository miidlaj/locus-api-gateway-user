package com.midlaj.apiGatewayUser.controller;

import com.midlaj.apiGatewayUser.exception.ResourceNotFoundException;
import com.midlaj.apiGatewayUser.model.User;
import com.midlaj.apiGatewayUser.repository.UserRepository;
import com.midlaj.apiGatewayUser.security.CurrentUser;
import com.midlaj.apiGatewayUser.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}

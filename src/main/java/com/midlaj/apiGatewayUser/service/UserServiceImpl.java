package com.midlaj.apiGatewayUser.service;

import com.midlaj.apiGatewayUser.model.AuthProvider;
import com.midlaj.apiGatewayUser.payload.ApiResponse;
import com.midlaj.apiGatewayUser.payload.SignUpRequest;
import com.midlaj.apiGatewayUser.repository.RoleRepository;
import com.midlaj.apiGatewayUser.repository.UserRepository;
import com.midlaj.apiGatewayUser.model.Role;
import com.midlaj.apiGatewayUser.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static Long NORMAL_USER_ID = Long.valueOf(1);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<? extends Object> saveUser(SignUpRequest signUpRequest) {

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
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        System.out.println(location);

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

//    @Override
//    public Optional<User> findByUsername(String username) {
//        log.info("Inside the findByUsername method in UserService");
//
//        return userRepository.findByUsername(username);
//    }

}

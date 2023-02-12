//package com.midlaj.apiGatewayUser.security;
//
//import com.midlaj.apiGatewayUser.model.Role;
//import com.midlaj.apiGatewayUser.model.User;
//import com.midlaj.apiGatewayUser.service.UserService;
//import com.midlaj.apiGatewayUser.old.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class CustomUserDetailService implements UserDetailsService {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userService.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        Set<Role> roles = user.getRoles();
//
//        Set<GrantedAuthority> authorities = new HashSet<>();
//
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//
//
//
//        return UserPrinciple.builder()
//                .user(user)
//                .id(user.getId())
//                .username(username)
//                .password(user.getPassword())
//                .authorities(authorities)
//                .build();
//    }
//}

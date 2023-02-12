//package com.midlaj.apiGatewayUser.security;
//
//import com.midlaj.apiGatewayUser.model.Role;
//import com.midlaj.apiGatewayUser.model.User;
//import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UserPrinciple implements UserDetails {
//
//    private Long id;
//
//    private String username;
//
//    transient private String password;
//
//    transient private User user;
//
//    private boolean enabled;
//
//    private Set<GrantedAuthority> authorities;
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return user.isEnabled();
//    }
//}

//package com.midlaj.apiGatewayUser.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "username", unique = true, nullable = false, length = 100)
//    private String username;
//
//    @Column(name = "password", nullable = false, length = 64)
//    private String password;
//
//    @Column(name = "first_name", nullable = false, length = 64)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false, length = 64)
//    private String lastName;
//
//    @Column(name = "created_time", nullable = false)
//    private LocalDateTime createdTime;
//
//    private boolean enabled;
//
//    @Column(name = "phone", nullable = false, length = 32)
//    private String phone;
//
//    @Column(name = "reset_password_token", length = 30)
//    private String resetPasswordToken;
//
//    @Column(name = "verification_code",length = 64)
//    private String verificationCode;
//
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private AuthProvider provider;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();
//
//
//}

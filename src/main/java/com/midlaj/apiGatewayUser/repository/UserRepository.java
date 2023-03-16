package com.midlaj.apiGatewayUser.repository;

import com.midlaj.apiGatewayUser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode =?1")
    User findUserByVerificationCode(String verificationCode);

    @Query("SELECT u FROM User u WHERE u.resetPasswordToken =?1")
    User findUserByResetPasswordToken(String resetPasswordToken);
}

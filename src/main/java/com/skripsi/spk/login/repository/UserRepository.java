package com.skripsi.spk.login.repository;

import com.skripsi.spk.login.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query("""
        UPDATE User u 
        SET u.resetToken = :token, 
            u.resetTokenExpiredAt = :expiredAt
        WHERE u.email = :email
    """)
    int updateResetTokenByEmail(
            String email,
            String token,
            LocalDateTime expiredAt
    );

    @Modifying
    @Query("""
        UPDATE User u 
        SET u.password = :password,
            u.resetToken = NULL,
            u.resetTokenExpiredAt = NULL
        WHERE u.resetToken = :token
    """)
    int updatePasswordByResetToken(String token, String password);
}

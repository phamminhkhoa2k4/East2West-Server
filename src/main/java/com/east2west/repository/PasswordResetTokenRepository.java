package com.east2west.repository;

import com.east2west.models.Entity.PasswordResetToken;
import com.east2west.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    void deleteByExpiryDateLessThan(Date now);
    void deleteByUser(User user);

    Optional<PasswordResetToken> findByUser_UserId(int userId);



}




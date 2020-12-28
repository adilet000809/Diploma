package com.diploma.app.repository;

import com.diploma.app.model.PasswordResetToken;
import com.diploma.app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(Users user);
    void deleteAllByExpirationLessThan(Date now);
    void deleteAllByUser(Users user);
}

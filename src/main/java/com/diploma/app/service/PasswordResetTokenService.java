package com.diploma.app.service;

import com.diploma.app.model.PasswordResetToken;
import com.diploma.app.model.Users;

import java.util.Date;

public interface PasswordResetTokenService {

    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(Users user);
    void deleteAllByExpirationLessThan(Date now);
    void deleteAllByUser(Users user);
    void save(PasswordResetToken passwordResetToken);
}

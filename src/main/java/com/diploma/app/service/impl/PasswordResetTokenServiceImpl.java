package com.diploma.app.service.impl;

import com.diploma.app.model.PasswordResetToken;
import com.diploma.app.model.Users;
import com.diploma.app.repository.PasswordResetTokenRepository;
import com.diploma.app.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public PasswordResetToken findByUser(Users user) {
        return passwordResetTokenRepository.findByUser(user);
    }

    @Override
    public void deleteAllByExpirationLessThan(Date now) {
        passwordResetTokenRepository.deleteAllByExpirationLessThan(now);
    }

    @Override
    public void deleteAllByUser(Users user) {
        passwordResetTokenRepository.deleteAllByUser(user);
    }

    @Override
    public void save(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }
}

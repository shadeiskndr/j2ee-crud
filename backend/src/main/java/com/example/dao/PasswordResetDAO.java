package com.example.dao;

import com.example.model.PasswordReset;

public interface PasswordResetDAO {
    int save(PasswordReset passwordReset);
    PasswordReset findByToken(String token);
    PasswordReset findByVerificationCode(String verificationCode);
    boolean markAsUsed(String token);
    boolean deleteExpiredTokens();
    boolean deleteByUserId(int userId);
}

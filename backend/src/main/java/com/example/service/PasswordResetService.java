package com.example.service;

import com.example.dao.PasswordResetDAO;
import com.example.dao.PasswordResetDAOImpl;
import com.example.dao.UserDAO;
import com.example.dao.UserDAOImpl;
import com.example.model.PasswordReset;
import com.example.model.User;
import com.example.util.PasswordResetUtil;
import com.example.util.PasswordUtil;

import java.time.LocalDateTime;

public class PasswordResetService {
    private final PasswordResetDAO passwordResetDAO = new PasswordResetDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final EmailService emailService = new MailerSendEmailService();

    public boolean initiatePasswordReset(String email) {
        
        // Find user by email
        User user = userDAO.findByEmail(email);
        if (user == null) {
            // For security, don't reveal if email exists
            return true;
        }

        try {
            // Clean up any existing reset tokens for this user
            passwordResetDAO.deleteByUserId(user.getId());

            // Generate new reset token and verification code
            String resetToken = PasswordResetUtil.generateResetToken();
            String verificationCode = PasswordResetUtil.generateVerificationCode();
            
            // Calculate expiration time
            long expirationMs = PasswordResetUtil.getExpirationTime();
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationMs / 1000);

            // Create password reset record
            PasswordReset passwordReset = new PasswordReset(
                user.getId(), 
                resetToken, 
                verificationCode, 
                expiresAt
            );

            // Save to database
            int resetId = passwordResetDAO.save(passwordReset);
            if (resetId == 0) {
                System.err.println("Failed to save password reset record to database");
                return false;
            }

            // Send email with verification code
            boolean emailSent = emailService.sendPasswordResetEmail(
                user.getEmail(), 
                user.getUsername(), 
                verificationCode
            );
            
            if (!emailSent) {
                System.err.println("Failed to send password reset email");
                // Optionally, you might want to delete the reset record if email fails
                passwordResetDAO.markAsUsed(resetToken);
            }
            
            return emailSent;
            
        } catch (Exception e) {
            System.err.println("Error in initiatePasswordReset: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String verifyCodeAndGetToken(String verificationCode) {
        PasswordReset passwordReset = passwordResetDAO.findByVerificationCode(verificationCode);
        
        if (passwordReset == null || passwordReset.isExpired()) {
            return null;
        }

        return passwordReset.getToken();
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordReset passwordReset = passwordResetDAO.findByToken(token);
        
        if (passwordReset == null || passwordReset.isExpired() || passwordReset.isUsed()) {
            return false;
        }

        // Get user by ID
        User user = userDAO.findById(passwordReset.getUserId());
        if (user == null) {
            return false;
        }

        // Hash new password
        String hashedPassword = PasswordUtil.hash(newPassword);

        // Update user password
        boolean passwordUpdated = userDAO.updatePassword(user.getId(), hashedPassword);
        
        if (passwordUpdated) {
            // Mark reset token as used
            passwordResetDAO.markAsUsed(token);
            return true;
        }

        return false;
    }

    public void cleanupExpiredTokens() {
        passwordResetDAO.deleteExpiredTokens();
    }
}

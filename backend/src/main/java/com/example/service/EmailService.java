package com.example.service;

public interface EmailService {
    boolean sendPasswordResetEmail(String toEmail, String toName, String verificationCode);
}

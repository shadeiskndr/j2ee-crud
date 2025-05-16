package com.example.util;

import java.security.SecureRandom;
import java.util.UUID;

public class PasswordResetUtil {
    private static final SecureRandom random = new SecureRandom();
    
    public static String generateResetToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateVerificationCode() {
        // Generate 6-digit verification code
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
    public static long getExpirationTime() {
        String expirationEnv = System.getenv("PASSWORD_RESET_EXPIRATION");
        return (expirationEnv != null) 
            ? Long.parseLong(expirationEnv) 
            : 15 * 60 * 1000; // default 15 minutes
    }
}

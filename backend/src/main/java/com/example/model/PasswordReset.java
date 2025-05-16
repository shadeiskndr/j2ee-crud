package com.example.model;

import java.time.LocalDateTime;

public class PasswordReset {
    private int id;
    private int userId;
    private String token;
    private String verificationCode;
    private LocalDateTime expiresAt;
    private boolean used;
    private LocalDateTime createdAt;

    // Constructors
    public PasswordReset() {}

    public PasswordReset(int userId, String token, String verificationCode, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.verificationCode = verificationCode;
        this.expiresAt = expiresAt;
        this.used = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

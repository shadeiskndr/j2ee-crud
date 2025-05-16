package com.example.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MailerSendEmailService implements EmailService {
    private static final String API_URL = "https://api.mailersend.com/v1/email";
    private static final String API_TOKEN = System.getenv("MAILERSEND_API_TOKEN");
    private static final String FROM_EMAIL = System.getenv("MAILERSEND_FROM_EMAIL");
    private static final String FROM_NAME = System.getenv("MAILERSEND_FROM_NAME");
    
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public boolean sendPasswordResetEmail(String toEmail, String toName, String verificationCode) {
        // Validate environment variables
        if (API_TOKEN == null || FROM_EMAIL == null) {
            System.err.println("MailerSend configuration missing. Check MAILERSEND_API_TOKEN and MAILERSEND_FROM_EMAIL environment variables.");
            return false;
        }

        try {
            JSONObject emailData = buildEmailPayload(toEmail, toName, verificationCode);
            
            RequestBody body = RequestBody.create(
                emailData.toString(),
                MediaType.get("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest") // Required by MailerSend
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .post(body)
                .build();
            
            try (Response response = client.newCall(request).execute()) {
                
                if (response.isSuccessful()) {
                    return true;
                } else {
                    // Log request details for debugging
                    System.err.println("Request URL: " + API_URL);
                    System.err.println("Request Headers: " + request.headers());
                    
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private JSONObject buildEmailPayload(String toEmail, String toName, String verificationCode) {
        JSONObject payload = new JSONObject();
        
        // From - simplified structure matching MailerSend API
        JSONObject from = new JSONObject();
        from.put("email", FROM_EMAIL);
        if (FROM_NAME != null && !FROM_NAME.trim().isEmpty()) {
            from.put("name", FROM_NAME);
        }
        payload.put("from", from);
        
        // To - array of recipients
        JSONArray to = new JSONArray();
        JSONObject recipient = new JSONObject();
        recipient.put("email", toEmail);
        if (toName != null && !toName.trim().isEmpty()) {
            recipient.put("name", toName);
        }
        to.put(recipient);
        payload.put("to", to);
        
        // Subject
        payload.put("subject", "Password Reset Verification Code");
        
        // Text Content (required)
        String textContent = buildTextContent(verificationCode);
        payload.put("text", textContent);
        
        // HTML Content (optional but recommended)
        String htmlContent = buildHtmlContent(verificationCode);
        payload.put("html", htmlContent);
        
        return payload;
    }

    private String buildHtmlContent(String verificationCode) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
                    .header { background-color: #007bff; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; }
                    .code { background-color: #f8f9fa; border: 2px solid #007bff; padding: 15px; font-size: 24px; font-weight: bold; text-align: center; letter-spacing: 3px; margin: 20px 0; border-radius: 5px; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; border-top: 1px solid #eee; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Password Reset Request</h1>
                </div>
                <div class="content">
                    <p>Hello,</p>
                    <p>You have requested to reset your password. Please use the verification code below to proceed:</p>
                    <div class="code">%s</div>
                    <p><strong>This code will expire in 15 minutes.</strong></p>
                    <p>If you did not request this password reset, please ignore this email.</p>
                    <p>For security reasons, do not share this code with anyone.</p>
                </div>
                <div class="footer">
                    <p>This is an automated message, please do not reply to this email.</p>
                </div>
            </body>
            </html>
            """, verificationCode);
    }

    private String buildTextContent(String verificationCode) {
        return String.format("""
            Password Reset Request
            
            Hello,
            
            You have requested to reset your password. Please use the verification code below to proceed:
            
            Verification Code: %s
            
            This code will expire in 15 minutes.
            
            If you did not request this password reset, please ignore this email.
            
            For security reasons, do not share this code with anyone.
            
            This is an automated message, please do not reply to this email.
            """, verificationCode);
    }
}

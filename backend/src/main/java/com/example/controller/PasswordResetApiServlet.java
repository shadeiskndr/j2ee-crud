package com.example.controller;

import com.example.service.PasswordResetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/auth/password-reset/*")
public class PasswordResetApiServlet extends HttpServlet {
    private final PasswordResetService passwordResetService = new PasswordResetService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        // Read request body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject body = new JSONObject(sb.toString());

        switch (path) {
            case "/initiate" -> handleInitiateReset(body, resp);
            case "/verify-code" -> handleVerifyCode(body, resp);
            case "/reset" -> handleResetPassword(body, resp);
            default -> {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Endpoint not found\"}");
            }
        }
    }

    private void handleInitiateReset(JSONObject body, HttpServletResponse resp) throws IOException {
        try {
            String email = body.getString("email");
            
            if (email == null || email.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Email is required\"}");
                return;
            }

            boolean success = passwordResetService.initiatePasswordReset(email);
            
            if (success) {
                resp.getWriter().write("{\"message\":\"If the email exists, a verification code has been sent\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Failed to initiate password reset\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid request format\"}");
        }
    }

    private void handleVerifyCode(JSONObject body, HttpServletResponse resp) throws IOException {
        try {
            String verificationCode = body.getString("verificationCode");
            
            if (verificationCode == null || verificationCode.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Verification code is required\"}");
                return;
            }

            String resetToken = passwordResetService.verifyCodeAndGetToken(verificationCode);
            
            if (resetToken != null) {
                JSONObject response = new JSONObject();
                response.put("message", "Verification code is valid");
                response.put("resetToken", resetToken);
                resp.getWriter().write(response.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid or expired verification code\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid request format\"}");
        }
    }

    private void handleResetPassword(JSONObject body, HttpServletResponse resp) throws IOException {
        try {
            String resetToken = body.getString("resetToken");
            String newPassword = body.getString("newPassword");
            
            if (resetToken == null || resetToken.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Reset token is required\"}");
                return;
            }
            
            if (newPassword == null || newPassword.length() < 6) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Password must be at least 6 characters long\"}");
                return;
            }

            boolean success = passwordResetService.resetPassword(resetToken, newPassword);
            
            if (success) {
                resp.getWriter().write("{\"message\":\"Password has been reset successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid or expired reset token\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid request format\"}");
        }
    }
}

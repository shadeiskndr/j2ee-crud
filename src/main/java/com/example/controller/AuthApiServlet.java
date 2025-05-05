package com.example.controller;

import com.example.dao.UserDAO;
import com.example.dao.UserDAOImpl;
import com.example.model.User;
import com.example.util.PasswordUtil;
import com.example.util.JwtUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthApiServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        JSONObject body = new JSONObject(sb.toString());

        if ("/register".equals(path)) {
            String username = body.getString("username");
            String email = body.getString("email");
            String password = body.getString("password");

            if (userDAO.findByUsername(username) != null || userDAO.findByEmail(email) != null) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"error\":\"Username or email already exists\"}");
                return;
            }

            String hash = PasswordUtil.hash(password);
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(hash);
            int id = userDAO.save(user);

            if (id > 0) {
                resp.getWriter().write("{\"message\":\"Registration successful\"}");
            } else {
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Registration failed\"}");
            }
        } else if ("/login".equals(path)) {
            String username = body.getString("username");
            String password = body.getString("password");

            User user = userDAO.findByUsername(username);
            if (user == null || !PasswordUtil.verify(user.getPassword(), password)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\":\"Invalid credentials\"}");
                return;
            }

            String token = JwtUtil.generateToken(username);
            resp.getWriter().write("{\"token\":\"" + token + "\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

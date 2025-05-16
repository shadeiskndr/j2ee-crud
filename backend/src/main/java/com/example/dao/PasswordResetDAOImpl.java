package com.example.dao;

import com.example.model.PasswordReset;
import com.example.util.DBUtil;

import java.sql.*;

public class PasswordResetDAOImpl implements PasswordResetDAO {

    @Override
    public int save(PasswordReset passwordReset) {
        String sql = "INSERT INTO password_resets (user_id, token, verification_code, expires_at, used, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, passwordReset.getUserId());
            stmt.setString(2, passwordReset.getToken());
            stmt.setString(3, passwordReset.getVerificationCode());
            stmt.setTimestamp(4, Timestamp.valueOf(passwordReset.getExpiresAt()));
            stmt.setBoolean(5, passwordReset.isUsed());
            stmt.setTimestamp(6, Timestamp.valueOf(passwordReset.getCreatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return 0;
            
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public PasswordReset findByToken(String token) {
        String sql = "SELECT * FROM password_resets WHERE token = ? AND used = false";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPasswordReset(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PasswordReset findByVerificationCode(String verificationCode) {
        String sql = "SELECT * FROM password_resets WHERE verification_code = ? AND used = false";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, verificationCode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPasswordReset(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean markAsUsed(String token) {
        String sql = "UPDATE password_resets SET used = true WHERE token = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteExpiredTokens() {
        String sql = "DELETE FROM password_resets WHERE expires_at < NOW()";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return stmt.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteByUserId(int userId) {
        String sql = "DELETE FROM password_resets WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private PasswordReset mapResultSetToPasswordReset(ResultSet rs) throws SQLException {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setId(rs.getInt("id"));
        passwordReset.setUserId(rs.getInt("user_id"));
        passwordReset.setToken(rs.getString("token"));
        passwordReset.setVerificationCode(rs.getString("verification_code"));
        passwordReset.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        passwordReset.setUsed(rs.getBoolean("used"));
        passwordReset.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return passwordReset;
    }
}

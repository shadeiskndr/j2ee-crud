package com.example.dao;

import com.example.model.Postcode;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostcodeDAOImpl implements PostcodeDAO {
    @Override
    public List<Postcode> getAllPostcodes() throws SQLException {
        List<Postcode> postcodes = new ArrayList<>();
        String sql = "SELECT * FROM postcodes ORDER BY postcode ASC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                postcodes.add(new Postcode(
                    rs.getString("postcode"),
                    rs.getString("town"),
                    rs.getString("state_code")
                ));
            }
        }
        return postcodes;
    }

    @Override
    public List<Postcode> getPostcodesPaginated(int pageIndex, int pageSize) throws SQLException {
        List<Postcode> postcodes = new ArrayList<>();
        String sql = "SELECT * FROM postcodes ORDER BY postcode ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, pageIndex * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    postcodes.add(new Postcode(
                        rs.getString("postcode"),
                        rs.getString("town"),
                        rs.getString("state_code")
                    ));
                }
            }
        }
        return postcodes;
    }

    @Override
    public int getPostcodesCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM postcodes";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Postcode> searchPostcodes(String search, int pageIndex, int pageSize) throws SQLException {
        List<Postcode> postcodes = new ArrayList<>();
        String sql = "SELECT * FROM postcodes WHERE postcode LIKE ? ORDER BY postcode ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + search + "%";
            ps.setString(1, pattern);
            ps.setInt(2, pageSize);
            ps.setInt(3, pageIndex * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    postcodes.add(new Postcode(
                        rs.getString("postcode"),
                        rs.getString("town"),
                        rs.getString("state_code")
                    ));
                }
            }
        }
        return postcodes;
    }
    
    @Override
    public int getSearchPostcodesCount(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM postcodes WHERE postcode LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + search + "%";
            ps.setString(1, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }    
}

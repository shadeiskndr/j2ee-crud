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
}

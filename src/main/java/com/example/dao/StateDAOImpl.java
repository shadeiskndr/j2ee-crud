package com.example.dao;

import com.example.model.State;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StateDAOImpl implements StateDAO {
    @Override
    public List<State> getAllStates() throws SQLException {
        List<State> states = new ArrayList<>();
        String sql = "SELECT * FROM states ORDER BY state_code ASC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                states.add(new State(
                    rs.getString("state_code"),
                    rs.getString("state_name")
                ));
            }
        }
        return states;
    }
}

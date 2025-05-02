package com.example.dao;

import com.example.model.State;
import java.sql.SQLException;
import java.util.List;

public interface StateDAO {
    List<State> getAllStates() throws SQLException;
}

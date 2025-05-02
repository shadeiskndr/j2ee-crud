package com.example.dao;

import com.example.model.Postcode;
import java.sql.SQLException;
import java.util.List;

public interface PostcodeDAO {
    List<Postcode> getAllPostcodes() throws SQLException;
    // Optionally: Postcode getPostcode(String postcode) throws SQLException;
}

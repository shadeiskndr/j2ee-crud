package com.example.dao;

import com.example.model.Postcode;
import java.sql.SQLException;
import java.util.List;

public interface PostcodeDAO {
    List<Postcode> getAllPostcodes() throws SQLException;

    // Pagination support
    List<Postcode> getPostcodesPaginated(int pageIndex, int pageSize) throws SQLException;
    int getPostcodesCount() throws SQLException;

    List<Postcode> searchPostcodes(String search, int pageIndex, int pageSize) throws SQLException;
    int getSearchPostcodesCount(String search) throws SQLException;
}

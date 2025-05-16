package com.example.service;

import com.example.model.Postcode;
import java.util.List;

public interface PostcodeService {
    List<Postcode> getAllPostcodes();

    // Pagination support
    List<Postcode> getPostcodesPaginated(int pageIndex, int pageSize);
    int getPostcodesCount();

    List<Postcode> searchPostcodes(String search, int pageIndex, int pageSize);
    int getSearchPostcodesCount(String search);
}

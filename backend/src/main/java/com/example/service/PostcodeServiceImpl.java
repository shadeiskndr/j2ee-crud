package com.example.service;

import com.example.dao.PostcodeDAO;
import com.example.dao.PostcodeDAOImpl;
import com.example.model.Postcode;

import java.util.ArrayList;
import java.util.List;

public class PostcodeServiceImpl implements PostcodeService {
    private final PostcodeDAO postcodeDAO;

    public PostcodeServiceImpl() {
        this.postcodeDAO = new PostcodeDAOImpl();
    }

    @Override
    public List<Postcode> getAllPostcodes() {
        try {
            return postcodeDAO.getAllPostcodes();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Postcode> getPostcodesPaginated(int pageIndex, int pageSize) {
        try {
            return postcodeDAO.getPostcodesPaginated(pageIndex, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int getPostcodesCount() {
        try {
            return postcodeDAO.getPostcodesCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Postcode> searchPostcodes(String search, int pageIndex, int pageSize) {
        try {
            return postcodeDAO.searchPostcodes(search, pageIndex, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int getSearchPostcodesCount(String search) {
        try {
            return postcodeDAO.getSearchPostcodesCount(search);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

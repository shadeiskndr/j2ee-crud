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
}

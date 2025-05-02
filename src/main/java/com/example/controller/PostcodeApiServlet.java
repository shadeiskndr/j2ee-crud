package com.example.controller;

import com.example.model.Postcode;
import com.example.service.PostcodeService;
import com.example.service.PostcodeServiceImpl;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/postcodes")
public class PostcodeApiServlet extends HttpServlet {

    private final PostcodeService postcodeService = new PostcodeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            List<Postcode> postcodes = postcodeService.getAllPostcodes();
            JSONArray arr = new JSONArray();
            for (Postcode pc : postcodes) {
                JSONObject obj = new JSONObject();
                obj.put("postcode", pc.getPostcode());
                obj.put("town", pc.getTown());
                obj.put("state_code", pc.getStateCode());
                arr.put(obj);
            }
            resp.getWriter().write(arr.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

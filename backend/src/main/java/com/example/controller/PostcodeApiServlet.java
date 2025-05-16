package com.example.controller;

import com.example.model.Postcode;
import com.example.service.PostcodeService;
import com.example.service.PostcodeServiceImpl;
import com.example.util.AuthUtil;

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
        if (AuthUtil.authenticate(req, resp) == null) return;
        resp.setContentType("application/json");
        try {
            int pageSize = 10;
            int pageIndex = 0;
            String pageSizeParam = req.getParameter("pageSize");
            String pageIndexParam = req.getParameter("pageIndex");
            if (pageSizeParam != null) {
                try { pageSize = Integer.parseInt(pageSizeParam); } catch (NumberFormatException ignored) {}
            }
            if (pageIndexParam != null) {
                try { pageIndex = Integer.parseInt(pageIndexParam); } catch (NumberFormatException ignored) {}
            }
    
            String search = req.getParameter("search");
            List<Postcode> postcodes;
            int totalCount;
    
            if (search != null && !search.trim().isEmpty()) {
                postcodes = postcodeService.searchPostcodes(search.trim(), pageIndex, pageSize);
                totalCount = postcodeService.getSearchPostcodesCount(search.trim());
            } else {
                postcodes = postcodeService.getPostcodesPaginated(pageIndex, pageSize);
                totalCount = postcodeService.getPostcodesCount();
            }
    
            JSONArray arr = new JSONArray();
            for (Postcode pc : postcodes) {
                JSONObject obj = new JSONObject();
                obj.put("postcode", pc.getPostcode());
                obj.put("town", pc.getTown());
                obj.put("state_code", pc.getStateCode());
                arr.put(obj);
            }
    
            JSONObject result = new JSONObject();
            result.put("postcodes", arr);
            result.put("totalCount", totalCount);
            result.put("pageIndex", pageIndex);
            result.put("pageSize", pageSize);
    
            resp.getWriter().write(result.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }    
}

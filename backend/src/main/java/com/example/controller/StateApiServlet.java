package com.example.controller;

import com.example.model.State;
import com.example.service.StateService;
import com.example.service.StateServiceImpl;
import com.example.util.AuthUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/states")
public class StateApiServlet extends HttpServlet {

    private final StateService stateService = new StateServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (AuthUtil.authenticate(req, resp) == null) return;
        resp.setContentType("application/json");
        try {
            List<State> states = stateService.getAllStates();
            JSONArray arr = new JSONArray();
            for (State state : states) {
                JSONObject obj = new JSONObject();
                obj.put("state_code", state.getStateCode());
                obj.put("state_name", state.getStateName());
                arr.put(obj);
            }
            resp.getWriter().write(arr.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

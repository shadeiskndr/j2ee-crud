package com.example.controller;

import com.example.model.Member;
import com.example.service.MemberService;
import com.example.service.MemberServiceImpl;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Date;
import java.util.List;

import org.json.*;

@WebServlet("/api/members/*")
public class MemberApiServlet extends HttpServlet {
    
    private final MemberService memberService;
    
    public MemberApiServlet() {
        this.memberService = new MemberServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all members
                List<Member> members = memberService.getAllMembers();
                JSONArray arr = new JSONArray();
                
                for (Member member : members) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", member.getId());
                    obj.put("name", member.getName());
                    obj.put("email", member.getEmail());
                    obj.put("join_date", member.getJoinDate().toString());
                    arr.put(obj);
                }
                
                resp.getWriter().write(arr.toString());
            } else {
                // Get member by ID
                try {
                    int id = Integer.parseInt(pathInfo.substring(1));
                    Member member = memberService.getMemberById(id);
                    
                    if (member != null) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", member.getId());
                        obj.put("name", member.getName());
                        obj.put("email", member.getEmail());
                        obj.put("join_date", member.getJoinDate().toString());
                        resp.getWriter().write(obj.toString());
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        
        try {
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            JSONObject body = new JSONObject(sb.toString());
            
            String name = body.getString("name");
            String email = body.getString("email");
            String joinDateStr = body.getString("join_date");
            
            Member member = new Member();
            member.setName(name);
            member.setEmail(email);
            member.setJoinDate(Date.valueOf(joinDateStr));
            
            int id = memberService.addMember(member);
            
            if (id > 0) {
                resp.getWriter().write("{\"id\":" + id + "}");
            } else {
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Failed to add member\"}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            JSONObject body = new JSONObject(sb.toString());
            
            String name = body.getString("name");
            String email = body.getString("email");
            String joinDateStr = body.getString("join_date");
            
            Member member = new Member();
            member.setId(id);
            member.setName(name);
            member.setEmail(email);
            member.setJoinDate(Date.valueOf(joinDateStr));
            
            boolean updated = memberService.updateMember(member);
            
            if (!updated) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = memberService.deleteMember(id);
            
            if (!deleted) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

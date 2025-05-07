package com.example.controller;

import com.example.model.Member;
import com.example.service.MemberService;
import com.example.service.MemberServiceImpl;
import com.example.util.AuthUtil;

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
        if (AuthUtil.authenticate(req, resp) == null) return;
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Pagination parameters
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

                List<Member> members = memberService.getMembersPaginated(pageIndex, pageSize);
                int totalCount = memberService.getMembersCount();

                JSONArray arr = new JSONArray();
                for (Member member : members) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", member.getId());
                    obj.put("name", member.getName());
                    obj.put("email", member.getEmail());
                    obj.put("join_date", member.getJoinDate().toString());
                    obj.put("ic_number", member.getIcNumber());
                    obj.put("gender", member.getGender());
                    obj.put("date_of_birth", member.getDateOfBirth().toString());
                    obj.put("postcode", member.getPostcode());
                    obj.put("town", member.getTown());
                    arr.put(obj);
                }

                JSONObject result = new JSONObject();
                result.put("members", arr);
                result.put("totalCount", totalCount);
                result.put("pageIndex", pageIndex);
                result.put("pageSize", pageSize);

                resp.getWriter().write(result.toString());
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
                        obj.put("ic_number", member.getIcNumber());
                        obj.put("gender", member.getGender());
                        obj.put("date_of_birth", member.getDateOfBirth().toString());
                        obj.put("postcode", member.getPostcode());
                        obj.put("town", member.getTown());
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
        if (AuthUtil.authenticate(req, resp) == null) return;
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
            String icNumber = body.getString("ic_number");
            String gender = body.getString("gender");
            String dateOfBirthStr = body.getString("date_of_birth");
            String postcode = body.getString("postcode");
            String town = body.getString("town");
            
            Member member = new Member();
            member.setName(name);
            member.setEmail(email);
            member.setJoinDate(Date.valueOf(joinDateStr));
            member.setIcNumber(icNumber);
            member.setGender(gender);
            member.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            member.setPostcode(postcode);
            member.setTown(town);
            
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
        if (AuthUtil.authenticate(req, resp) == null) return;
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
            String icNumber = body.getString("ic_number");
            String gender = body.getString("gender");
            String dateOfBirthStr = body.getString("date_of_birth");
            String postcode = body.getString("postcode");
            String town = body.getString("town");
            
            Member member = new Member();
            member.setId(id);
            member.setName(name);
            member.setEmail(email);
            member.setJoinDate(Date.valueOf(joinDateStr));
            member.setIcNumber(icNumber);
            member.setGender(gender);
            member.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            member.setPostcode(postcode);
            member.setTown(town);
            
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
        if (AuthUtil.authenticate(req, resp) == null) return;
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

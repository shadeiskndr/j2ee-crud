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

@WebServlet("/members/*")
public class MemberServlet extends HttpServlet {
    
    private final MemberService memberService;
    
    public MemberServlet() {
        this.memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all members
            List<Member> members = memberService.getAllMembers();
            req.setAttribute("members", members);
            req.getRequestDispatcher("/WEB-INF/views/list-members.jsp").forward(req, resp);
        } else if (pathInfo.equals("/add")) {
            // Show add form
            req.getRequestDispatcher("/WEB-INF/views/add-member.jsp").forward(req, resp);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show edit form
            try {
                int id = Integer.parseInt(pathInfo.substring(6));
                Member member = memberService.getMemberById(id);
                
                if (member != null) {
                    req.setAttribute("member", member);
                    req.getRequestDispatcher("/WEB-INF/views/edit-member.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("add".equals(action)) {
            // Add a new member
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String joinDateStr = req.getParameter("join_date");
            
            Member member = new Member();
            member.setName(name);
            member.setEmail(email);
            member.setJoinDate(Date.valueOf(joinDateStr));
            
            memberService.addMember(member);
            resp.sendRedirect(req.getContextPath() + "/members");
        } else if ("edit".equals(action)) {
            // Update an existing member
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String joinDateStr = req.getParameter("join_date");
                
                Member member = new Member();
                member.setId(id);
                member.setName(name);
                member.setEmail(email);
                member.setJoinDate(Date.valueOf(joinDateStr));
                
                memberService.updateMember(member);
                resp.sendRedirect(req.getContextPath() + "/members");
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("delete".equals(action)) {
            // Delete a member
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                memberService.deleteMember(id);
                resp.sendRedirect(req.getContextPath() + "/members");
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}

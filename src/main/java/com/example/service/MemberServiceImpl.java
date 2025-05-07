package com.example.service;

import com.example.dao.MemberDAO;
import com.example.dao.MemberDAOImpl;
import com.example.model.Member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberServiceImpl implements MemberService {
    
    private final MemberDAO memberDAO;
    
    public MemberServiceImpl() {
        this.memberDAO = new MemberDAOImpl();
    }
    
    @Override
    public List<Member> getAllMembers() {
        try {
            return memberDAO.getAllMembers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public Member getMemberById(int id) {
        try {
            return memberDAO.getMemberById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int addMember(Member member) {
        try {
            return memberDAO.addMember(member);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public boolean updateMember(Member member) {
        try {
            return memberDAO.updateMember(member);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteMember(int id) {
        try {
            return memberDAO.deleteMember(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Member> getMembersPaginated(int pageIndex, int pageSize) {
        try {
            return memberDAO.getMembersPaginated(pageIndex, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int getMembersCount() {
        try {
            return memberDAO.getMembersCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

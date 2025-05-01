package com.example.dao;

import com.example.model.Member;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    @Override
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Member member = new Member(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("join_date"),
                    rs.getString("ic_number"),
                    rs.getString("gender"),
                    rs.getDate("date_of_birth"),
                    rs.getString("postcode"),
                    rs.getString("town")
                );
                members.add(member);
            }
        }

        return members;
    }

    @Override
    public Member getMemberById(int id) throws SQLException {
        String sql = "SELECT * FROM members WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("join_date"),
                        rs.getString("ic_number"),
                        rs.getString("gender"),
                        rs.getDate("date_of_birth"),
                        rs.getString("postcode"),
                        rs.getString("town")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public int addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email, join_date, ic_number, gender, date_of_birth, postcode, town) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setDate(3, member.getJoinDate());
            ps.setString(4, member.getIcNumber());
            ps.setString(5, member.getGender());
            ps.setDate(6, member.getDateOfBirth());
            ps.setString(7, member.getPostcode());
            ps.setString(8, member.getTown());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    @Override
    public boolean updateMember(Member member) throws SQLException {
        String sql = "UPDATE members SET name=?, email=?, join_date=?, ic_number=?, gender=?, date_of_birth=?, postcode=?, town=? WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setDate(3, member.getJoinDate());
            ps.setString(4, member.getIcNumber());
            ps.setString(5, member.getGender());
            ps.setDate(6, member.getDateOfBirth());
            ps.setString(7, member.getPostcode());
            ps.setString(8, member.getTown());
            ps.setInt(9, member.getId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    @Override
    public boolean deleteMember(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}

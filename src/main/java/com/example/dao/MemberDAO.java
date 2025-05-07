package com.example.dao;

import com.example.model.Member;
import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {
    List<Member> getAllMembers() throws SQLException;
    Member getMemberById(int id) throws SQLException;
    int addMember(Member member) throws SQLException;
    boolean updateMember(Member member) throws SQLException;
    boolean deleteMember(int id) throws SQLException;

    // Pagination, search, and sorting support
    List<Member> getMembersPaginated(
        int offset, int pageSize,
        String search,
        String sortField,
        String sortOrder
    ) throws SQLException;

    int getMembersCount(String search) throws SQLException;
}

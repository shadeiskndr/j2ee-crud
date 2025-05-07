package com.example.service;

import com.example.model.Member;
import java.util.List;

public interface MemberService {
    List<Member> getAllMembers();
    Member getMemberById(int id);
    int addMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(int id);

    // Pagination, search, and sorting support
    List<Member> getMembersPaginated(
        int offset, int pageSize,
        String search,
        String sortField,
        String sortOrder
    );
    int getMembersCount(String search);
}

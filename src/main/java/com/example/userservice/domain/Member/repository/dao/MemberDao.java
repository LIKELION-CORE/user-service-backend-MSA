package com.example.userservice.domain.Member.repository.dao;

import com.example.userservice.domain.Member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDao {
    // 간단한 crud 작성
    Member insertMember(Member member);

    Member findMemberByUserId(String memberId);

    Optional<Member> duplicateMemberCheck(String memberId);

    void deleteById(Long memberId);

    List<Member> findAllMember();
}

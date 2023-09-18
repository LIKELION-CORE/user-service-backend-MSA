package com.example.userservice.domain.Member.repository.dao;

import com.example.userservice.domain.Member.entity.Member;

import java.util.Optional;

public interface MemberDao {
    // 간단한 crud 작성
    Member insertMember(Member member);

    Member findMemberByUserId(String userId);

    Optional<Member> duplicateMemberCheck(String userId);


//    Optional<Member> selectMember(Long number);
//
//    Member updateMember(Long number, String name) throws Exception;
//
//    void deleteMember (Long number) throws Exception;
}

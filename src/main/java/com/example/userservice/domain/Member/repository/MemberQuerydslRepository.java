package com.example.userservice.domain.Member.repository;

import com.example.userservice.domain.Member.entity.Member;

import java.util.Optional;

public interface MemberQuerydslRepository {
    Optional<Member> findMemberByUserId(String userId);
}

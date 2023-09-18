package com.example.userservice.domain.Member.repository;

import com.example.userservice.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long>,MemberQuerydslRepository {
    Optional<Member> findByUserId(String userId);
}
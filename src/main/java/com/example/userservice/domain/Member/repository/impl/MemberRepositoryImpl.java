package com.example.userservice.domain.Member.repository.impl;

import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.entity.QMember;
import com.example.userservice.domain.Member.repository.MemberQuerydslRepository;
import com.example.userservice.global.config.Querydsl4RepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.userservice.domain.Member.entity.QMember.member;
import static com.querydsl.jpa.JPAExpressions.select;


public class MemberRepositoryImpl extends Querydsl4RepositorySupport implements MemberQuerydslRepository {


    public MemberRepositoryImpl() {
        super(Member.class);
    }

    public Optional<Member> findMemberByUserId(String userId) {
        return Optional.ofNullable(select(member)
                .from(member)
                .where(member.userId.eq(userId).and(member.state.eq(true))).
                fetchOne());
    }
}

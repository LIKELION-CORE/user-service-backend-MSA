package com.example.userservice.domain.Member.repository.dao.impl;

import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.MemberRepository;
import com.example.userservice.domain.Member.repository.dao.MemberDao;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberDaoImpl implements MemberDao {

    private final MemberRepository memberRepository;
    @Override
    public Member insertMember(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    @Override
    public Member findMemberByUserId(String memberId) {
        Member member = memberRepository.findMemberByUserId(memberId).orElseThrow(() -> new NotFoundAccountException("계정을 찾을 수 없습니다"));
        return member;
    }

    @Override
    public Optional<Member> duplicateMemberCheck(String memberId) {
        return memberRepository.findByUserId(memberId);
    }

    public void deleteById(Long memberId) {
        memberRepository.deleteById(memberId);

    }

//    @Override
//    public Member updateMember(Long number, String name) throws Exception {
//        return null;
//    }
//
//    @Override
//    public void deleteMember(Long number) throws Exception {
//
//    }
}

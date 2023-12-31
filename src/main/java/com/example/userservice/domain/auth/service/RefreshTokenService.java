package com.example.userservice.domain.auth.service;


import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.MemberRepository;
import com.example.userservice.domain.auth.entity.RefreshToken;
import com.example.userservice.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void setRefreshToken(String username, String refreshToken) {
        Member member = memberRepository.findMemberByUserId(username).orElseThrow(() -> new RuntimeException("no member by username"));
        refreshTokenRepository.save(RefreshToken.builder().token(refreshToken).id(member.getUserId()).build());
    }
}
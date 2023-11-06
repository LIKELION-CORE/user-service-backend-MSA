package com.example.userservice.domain.Member.service.impl;

import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.MemberRepository;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.auth.jwt.MemberRole;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.exception.error.DuplicateAccountException;
import com.example.userservice.global.exception.error.EmailNotValidException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceImplTest {

    @Autowired
    EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("이메일 인증을 하지않고 회원가입을 하면 회원가입은 실패한다.")
    @Test
    void signUp(){

        //given
        SignUpRequestDto signUpRequestDto = createMemberThenReturnDto();

        //when //then
        assertThatThrownBy(()-> memberService.createMember(signUpRequestDto))
                .isInstanceOf(EmailNotValidException.class);

    }

    @DisplayName("회원가입을 시 아이디 중복확인을 실시한다.")
    @Test
    void createMemberWithDuplicateCheck(){

        //given
        Member member = createMemberThenReturnMemberEntity();

        //when //then
        memberRepository.save(member);

        SignUpRequestDto sinSignUpRequestDto = createMemberThenReturnDto();

        assertThatThrownBy(()-> memberService.createMember(sinSignUpRequestDto))
                .isInstanceOf(DuplicateAccountException.class)
                .hasMessage("아이디 중복");


    }

    @DisplayName("회원이 자기자신의 정보를 수정한다.")
    @Test
    void updateMember(){

        //given
        Member member = createMemberThenReturnMemberEntity();
        Member savedMember = memberRepository.save(member);

        UpdateMemberRequesstDto updateMemberRequesstDto = updateMemberThenReturnDto("010-4094-1234","kbsserver@naver.com","1234",MemberRole.LION,202184007);

        //when
        savedMember.updateMember(updateMemberRequesstDto);

        //then
        assertThat(savedMember)
                .extracting("phone", "name","password","memberRole")
                .containsExactlyInAnyOrder(
                        "010-4094-1234", "김민우","1234",MemberRole.LION
                );

    }
    @DisplayName("회원가입을 한 유저의 정보를 삭제한다.")
    @Test
    void deleteMember(){
        //given
        Member member = createMemberThenReturnMemberEntity();
        //when
        Member savedMember = memberRepository.save(member);
        Long memberId = memberService.deleteMember(savedMember.getUserId());

        //then
        assertThat(memberId).isNotNull();
        assertThat(memberRepository.findMemberByUserId(savedMember.getUserId())).isEmpty();
    }

    private static UpdateMemberRequesstDto updateMemberThenReturnDto(String phone,
                                                                     String email,
                                                                     String password,
                                                                     MemberRole memberRole,
                                                                     Integer studentId
        ) {
        UpdateMemberRequesstDto updateMemberRequesstDto = UpdateMemberRequesstDto.builder()
                .phone(phone)
                .email(email)
                .department(password)
                .memberRole(memberRole)
                .studentId(studentId)
                .build();
        return updateMemberRequesstDto;
    }
    private static SignUpRequestDto createMemberThenReturnDto() {
        return SignUpRequestDto.builder()
                .phone("010-1234-1234")
                .name("김민우")
                .password("1234")
                .email("kbsserver@naver.com")
                .build();

    }

    private static Member createMemberThenReturnMemberEntity() {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .phone("010-1234-1234")
                .name("김민우")
                .password("1234")
                .email("kbsserver@naver.com")
                .build();
        return signUpRequestDto.toEntity();
    }

}
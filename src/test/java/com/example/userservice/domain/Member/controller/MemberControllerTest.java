package com.example.userservice.domain.Member.controller;
import com.example.userservice.domain.Member.dto.request.MemberLoginRequestDto;
import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.restdoc.RestDocsBasic;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.Member.service.impl.MemberServiceImpl;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.jwt.MemberRole;
import com.example.userservice.global.common.CommonResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class MemberControllerTest extends RestDocsBasic {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

    @MockBean
    MemberServiceImpl memberService;
    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    public void beforeAll() {
        em.persist(Member.builder()
                .userId("kbsserver@naver.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .state(true)
                .name("민우")
                .memberRole(MemberRole.APPLY)
                .phone("010-1234-1234")
                .department("ict공학부")
                .studentId(123412341)
                .build());
    }

    @DisplayName("회원가입 테스트")
    @Test
    void test() throws Exception {

        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .phone("010-1234-1234")
                .name("김민우")
                .password("alsdn1234")
                .userId("kbsserver@naver.com")
                .department("ict공학부")
                .studentId(123412341)
                .build();

        CreateMemberResponseDto createMemberResponseDto = CreateMemberResponseDto.builder()
                .phone("010-1234-1234")
                .name("김민우")
                .userId("kbsserver@naver.com")
                .department("ict공학부")
                .studentId(123412341)
                .build();


        Mockito.when(memberService.createMember(signUpRequestDto)).thenReturn(createMemberResponseDto);
        String url = "/api/v1/member";
        

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .with(csrf())
                )
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.code").exists())
                .andExpect(jsonPath("$.message").exists())
//                .andExpect(jsonPath("$.data").exists())
                .andDo(print())

                //docs
                .andDo(document("join",
                        requestFields(
                                fieldWithPath("userId").description("계정 아이디입력하는 필드"),
                                fieldWithPath("password").description("계정 비밀번호 입력하는 필드"),
                                fieldWithPath("name").description("실명을 입력하는 필드"),
                                fieldWithPath("email").description("이메일을 입력하는 필드"),
                                fieldWithPath("phone").description("전화번호를 입력하는 필드"),
                                fieldWithPath("studentId").description("학번을 입력하는 필드"),
                                fieldWithPath("department").description("학과를 입력하는 필드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("response code 1 == Success , 0 == Fail"),
                                fieldWithPath("message").description("응답 결과 값 메세지"),
                                subsectionWithPath("data").description("응답 데이터")
                        )
                ));

        verify(memberService).createMember(signUpRequestDto);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    @WithMockUser(username = "member1", roles = {"APPLY"})
    public void testUpdateMemberSuccess() throws Exception {
        // given
        String userId = "member1";

        UpdateMemberRequesstDto updateMemberRequesstDto = UpdateMemberRequesstDto.builder()
                .memberRole(MemberRole.LION)
                .name("민우")
                .password("alsdn12345")
                .phone("010-1234-1234")
                .build();

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMemberRequesstDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("member update",
                        requestFields(
                                fieldWithPath("name").description("이름 입력하는 필드"),
                                fieldWithPath("password").description("계정 비밀번호 입력하는 필드"),
                                fieldWithPath("phone").description("전화번호을 입력하는 필드"),
                                fieldWithPath("memberRole").description("역할 입력하는 필드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("response code 1 == Success , 0 == Fail"),
                                fieldWithPath("message").description("응답 결과 값 메세지"),
                                subsectionWithPath("data").description("응답 데이터")
                        )
                ));;

    }


    @Test
    @WithMockUser(username = "testUser", roles = {"APPLY"})
    @DisplayName("회원 정보 삭제 성공")
    public void testDeleteMemberSuccess() throws Exception {
        // given
        String url = "/api/v1/member";

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("member delete",
                        responseFields(
                                fieldWithPath("code").description("response code 1 == Success , 0 == Fail"),
                                fieldWithPath("message").description("응답 결과 값 메세지"),
                                subsectionWithPath("data").description("응답 데이터")
                        )
                ));

    }

    @Test
    @DisplayName("로그인 후 토큰이 정상적으로 발급이 되는지 확인한다.")
    public void usernamePasswordLogin() throws Exception {
        // given
        String username = "member1";
        String password = "1234";

        String url = "/api/v1/member/login";

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();
        // when then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("login",
                        requestFields(
                                fieldWithPath("username").description("사용자 아이디"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레시 토큰")
                        )
                )).andExpect(
                        cookie().exists("accessToken")
                ).andExpect(
                        cookie().exists("refreshToken")
                );
    }



}
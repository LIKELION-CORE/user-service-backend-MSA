//package com.example.userservice.domain.Member.controller;
//
//import com.example.userservice.domain.Member.dto.request.MemberLoginRequestDto;
//import com.example.userservice.domain.Member.entity.Member;
//import com.example.userservice.domain.Member.service.MemberService;
//import com.example.userservice.domain.auth.jwt.JwtProvider;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import javax.persistence.EntityManager;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = MemberController.class) // controller 관련 빈들만 올릴 수 있는 가벼운 빈
//class MemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private EntityManager em;
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    @BeforeEach
//    public void beforeAll() {
//        em.persist(Member.builder().userId("member1").password(new BCryptPasswordEncoder().encode("1234")).state(true).build());
//    }
//
//    @Test
//    @DisplayName("로그인 후 토큰이 정상적으로 발급이 되는지 확인한다.")
//    public void usernamePasswordLogin() throws Exception {
//        // given
//        String username = "member1";
//        String rawPassword = "1234";
//
//        MemberLoginRequestDto memberLoginRequestDto= MemberLoginRequestDto.builder()
//                .username(username)
//                .password(rawPassword)
//                .build();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(
//                post("/api/v1/member/login")
//                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
//        );
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(result -> System.out.println("result = " + result))
//                .andExpect(
//                        result -> {
//                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("accessToken").getValue())).isEqualTo(username);
//                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("refreshToken").getValue())).isEqualTo(username);
//                        }
//                );
//    }
//
//    @Test
//    @DisplayName("로그인 후 쿠키에 값이 제대로 저장되자않으면 실패한다.")
//    public void usernamePasswordLoginFail() throws Exception {
//        // given
//        String username = "member1";
//        String rawPassword = "wrongPassword";
//
//        MemberLoginRequestDto memberLoginRequestDto= MemberLoginRequestDto.builder()
//                .username(username)
//                .password(rawPassword)
//                .build();
//
//        // when
//        ResultActions resultActions = mockMvc.perform(
//                post("/api/v1/member/login")
//                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest())
//                .andExpect(result -> System.out.println("result = " + result))
//                .andExpect(
//                        cookie().doesNotExist("accessToken")
//                )
//                .andExpect(
//                        cookie().doesNotExist("refreshToken")
//                );
//
//    }
//
//
//}
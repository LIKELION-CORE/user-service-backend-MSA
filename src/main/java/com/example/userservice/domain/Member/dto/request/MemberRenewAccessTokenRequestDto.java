package com.example.userservice.domain.Member.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRenewAccessTokenRequestDto {
    String refreshToken;
}

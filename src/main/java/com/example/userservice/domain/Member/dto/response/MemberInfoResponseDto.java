package com.example.userservice.domain.Member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {
    private Long id;

    @Builder
    public MemberInfoResponseDto(Long id) {
        this.id = id;
    }
}

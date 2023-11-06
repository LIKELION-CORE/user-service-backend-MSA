package com.example.userservice.domain.notification.dto.request;

import com.example.userservice.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationEmailSaveRequestDto {
    @NotEmpty(message = "이름 설정은 필수입니다.")
    @Size(min = 1, max = 12, message = "닉네임을 12글자 이하로 설정헤주세요.")
    private String nickname;

    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 맞지 않습니다.")
    private String email;

    public Notification toEntity(NotificationEmailSaveRequestDto notificationEmailSaveRequestDto) {
        return  Notification.builder()
                .email(notificationEmailSaveRequestDto.getEmail())
                .nickname(notificationEmailSaveRequestDto.getNickname())
                .build();

    }
}

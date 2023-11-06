package com.example.userservice.domain.notification.dto.response;


import com.example.userservice.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationUserListResponseDto {
    private String name;
    private String email;

    public NotificationUserListResponseDto(Notification notification) {
        this.name = notification.getNickname();
        this.email = notification.getEmail();
    }
}

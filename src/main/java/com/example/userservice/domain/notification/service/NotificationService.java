package com.example.userservice.domain.notification.service;

import com.example.userservice.domain.notification.dto.request.NotificationEmailSaveRequestDto;
import com.example.userservice.domain.notification.dto.response.NotificationUserListResponseDto;
import com.example.userservice.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationService {
    Long saveEmail(NotificationEmailSaveRequestDto notificationEmailSaveRequestDto);


    List<NotificationUserListResponseDto> notificationUserList();
}

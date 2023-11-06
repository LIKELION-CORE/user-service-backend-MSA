package com.example.userservice.domain.notification.service.impl;

import com.example.userservice.domain.notification.dto.request.NotificationEmailSaveRequestDto;
import com.example.userservice.domain.notification.dto.response.NotificationUserListResponseDto;
import com.example.userservice.domain.notification.entity.Notification;
import com.example.userservice.domain.notification.repository.NotificationRepository;
import com.example.userservice.domain.notification.service.NotificationService;
import com.example.userservice.global.exception.error.DuplicateAccountException;
import com.example.userservice.global.exception.error.EmailNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.userservice.global.exception.ErrorCode.DUPLICATE_ACCOUNT_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    @Override
    @Transactional
    public Long saveEmail(NotificationEmailSaveRequestDto notificationEmailSaveRequestDto) {
        verifyEmail(notificationEmailSaveRequestDto);
        Notification notification = notificationEmailSaveRequestDto.toEntity(notificationEmailSaveRequestDto);
        Notification savedNotification = notificationRepository.save(notification);

        return  savedNotification.getId();
    }

    @Override
    public List<NotificationUserListResponseDto> notificationUserList() {
        List<Notification> notification = notificationRepository.findAll();
        List<NotificationUserListResponseDto> result = notification.stream().map(NotificationUserListResponseDto::new)
                .collect(Collectors.toList());

        return result;
    }

    private void verifyEmail(NotificationEmailSaveRequestDto notificationEmailSaveRequestDto) {
        Optional<Notification> email = notificationRepository.findByEmail(notificationEmailSaveRequestDto.getEmail());

        if(email.isPresent()){
            throw new DuplicateAccountException("중복된 계정입니다.");
        }
    }
}

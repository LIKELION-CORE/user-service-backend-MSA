package com.example.userservice.domain.notification.repository;

import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Optional<Notification> findByEmail(String email);

    List<Notification> findAll();
}

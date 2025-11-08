package com.vn.beta_testing.feature.notification.controller;

import com.vn.beta_testing.domain.Notification;
import com.vn.beta_testing.feature.notification.DTO.NotificationRequest;
import com.vn.beta_testing.feature.notification.service.NotificationService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    @ApiMessage("Get notifications by userId")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PostMapping("/create")
    @ApiMessage("Create notification for a user")
    public ResponseEntity<Notification> createNotification(
            @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(
                notificationService.createNotification(
                        request.getUserId(),
                        request.getTitle(),
                        request.getMessage(),
                        request.getType(),
                        request.getLink()));
    }

    @PutMapping("/{id}/read")
    @ApiMessage("Mark notification as read")
    public ResponseEntity<?> markAsRead(@PathVariable("id") Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete notification by ID")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}")
    @ApiMessage("Delete all notifications of a user")
    public ResponseEntity<?> deleteAllByUser(@PathVariable("userId") Long userId) {
        notificationService.deleteAllByUser(userId);
        return ResponseEntity.ok().build();
    }
}
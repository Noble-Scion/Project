package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.model.Notification;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.NotificationRepository;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.service.NotificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserAndReadOrderByCreatedAtDesc(user, false);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/mark-read/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-all-read/{userId}")
    public ResponseEntity<?> markAllNotificationsAsRead(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Notification> unreadNotifications = notificationRepository.findByUserAndReadOrderByCreatedAtDesc(user, false);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count-unread/{userId}")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        long count = notificationRepository.countByUserAndRead(user, false);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
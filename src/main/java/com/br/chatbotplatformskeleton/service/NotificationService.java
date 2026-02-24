package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Notification;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.NotificationDto;
import com.br.chatbotplatformskeleton.repository.NotificationRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                             UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public NotificationDto createNotification(NotificationDto dto, Long userId) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Notification notification = new Notification();
        notification.setUser(userOpt.get());
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setRelatedEntityType(dto.getRelatedEntityType());
        notification.setRelatedEntityId(dto.getRelatedEntityId());
        notification.setIsRead(false);

        // Set expiration to 30 days from now
        if (dto.getExpiresAt() == null) {
            notification.setExpiresAt(OffsetDateTime.now().plusDays(30));
        } else {
            notification.setExpiresAt(dto.getExpiresAt());
        }

        Notification saved = notificationRepository.save(notification);
        return toDto(saved);
    }

    public Optional<NotificationDto> findById(Long id) {
        return notificationRepository.findById(id).map(this::toDto);
    }

    public Page<NotificationDto> listByUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    public Page<NotificationDto> listUnreadByUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId, pageable).map(this::toDto);
    }

    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findUnreadByUserId(userId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public long countUnreadByUser(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public Optional<NotificationDto> markAsRead(Long notificationId) {
        Optional<Notification> opt = notificationRepository.findById(notificationId);
        if (opt.isEmpty()) return Optional.empty();

        Notification notification = opt.get();
        notification.setIsRead(true);
        notification.setReadAt(OffsetDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return Optional.of(toDto(saved));
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findUnreadByUserId(userId);
        OffsetDateTime now = OffsetDateTime.now();

        unread.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unread);
    }

    public Page<NotificationDto> listByType(Long userId, String type, Pageable pageable) {
        return notificationRepository.findByUserIdAndType(userId, type, pageable).map(this::toDto);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void cleanupExpiredNotifications() {
        notificationRepository.deleteExpiredNotifications();
    }

    private NotificationDto toDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser().getId());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setRelatedEntityType(notification.getRelatedEntityType());
        dto.setRelatedEntityId(notification.getRelatedEntityId());
        dto.setIsRead(notification.getIsRead());
        dto.setReadAt(notification.getReadAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setExpiresAt(notification.getExpiresAt());
        return dto;
    }
}


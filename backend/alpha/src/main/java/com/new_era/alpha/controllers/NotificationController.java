package com.new_era.alpha.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.services.player.NotificationService;

import lombok.AllArgsConstructor;

import com.new_era.alpha.controllers.dto.NotificationDTO;
import com.new_era.alpha.entities.player.Notification;
import com.new_era.alpha.security.UserSession;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserSession session;

    @PostMapping("/list")
    public ResponseEntity<List<NotificationDTO>> notifications() {
        Integer player_id = session.getPlayer_id();
        List<Notification> notifications = notificationService.getAllNotificationsByPlayerId(player_id);
        List<NotificationDTO> response = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO(
                notification.getTitle(),
                notification.getMessage(),
                notification.getCallback_for_accept(),
                notification.getCallback_for_decline()
            );

            response.add(notificationDTO);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

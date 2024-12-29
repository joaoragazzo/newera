package com.new_era.alpha.services.player;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.player.Notification;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.player.NotificationRepository;
import com.new_era.alpha.services.dto.ClanInvitationNotificationDTO;
import com.new_era.alpha.services.dto.ClanKikedDTO;
import com.new_era.alpha.services.dto.NotificationDTO;
import com.new_era.alpha.services.utils.NotificationFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final PlayerService playerService;
    
    public List<Notification> getAllNotificationsByPlayerId(Integer player_id) {
        return notificationRepository.findNotSeenNotificationByPlayerId(player_id);
    }

    public void sendNotification(NotificationDTO notification_dto) {
        Integer player_id = notification_dto.target_player_id();

        Player player = playerService.getPlayerById(player_id);

        Notification notification = buildNotification(notification_dto); 
        notification.setPlayer(player);

        notificationRepository.save(notification);
    }

    public Notification buildNotification(NotificationDTO notificationDTO) {
        
        Notification notification = new Notification();
        LocalDateTime now = LocalDateTime.now();

        notification.setTitle(notificationDTO.title());
        notification.setMessage(notificationDTO.message());
        notification.setHref(notificationDTO.href());
        notification.setCallback_for_accept(notificationDTO.callback_for_accept());
        notification.setCallback_for_decline(notificationDTO.callback_for_decline());
        notification.setIcon(notificationDTO.icon());
        notification.setCreated_at(now);
        
        return notification;
    }

    /*
     * A partir daqui, todos seguem o mesmo princípio: cria-se uma nova fabrica que formatará a mensagem, callback e informações 
     * e cria-se um método personalizado para cada um deles para chamar o método da fábrica e dar prosseguimento.
     */

    public void sendClanInviteNotification(ClanInvitationNotificationDTO inviteNotification) {
        NotificationDTO notification = NotificationFactory.createClanInviteNotification(inviteNotification);
        sendNotification(notification);
    }

    public void sendKickedFromClanNotification(ClanKikedDTO kikedNotification) {
        NotificationDTO notification = NotificationFactory.createKikedFromClanNotification(kikedNotification);
        sendNotification(notification);
    }
}

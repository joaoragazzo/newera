package com.new_era.alpha.repositories.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.new_era.alpha.entities.player.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.player.id = :player_id AND n.seen_at IS NULL")
    public List<Notification> findNotSeenNotificationByPlayerId(@Param("player_id") Integer player_id);

    // @Query("SELECT n FROM Notifiation n ORDER BY n.id DESC")
    // public Notification findLastNotification();
}

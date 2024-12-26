package com.new_era.alpha.repositories.player;

import org.springframework.data.jpa.repository.JpaRepository;

import com.new_era.alpha.entities.player.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    
}

package com.new_era.alpha.events.model;

import org.springframework.context.ApplicationEvent;

import com.new_era.alpha.entities.player.Notification;

import lombok.Getter;

@Getter
public class NotificationCreatedEvent extends ApplicationEvent {
    private final Notification notification;

    public NotificationCreatedEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }
}

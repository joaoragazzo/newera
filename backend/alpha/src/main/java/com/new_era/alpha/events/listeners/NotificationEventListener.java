package com.new_era.alpha.events.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.new_era.alpha.events.dto.NotificationStreamDTO;
import com.new_era.alpha.events.model.NotificationCreatedEvent;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@AllArgsConstructor
public class NotificationEventListener {
    private final Sinks.Many<NotificationStreamDTO> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<NotificationStreamDTO> getNotificationStream() {
        return notificationSink.asFlux();
    }

    @EventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        NotificationStreamDTO dto = new NotificationStreamDTO(
            event.getNotification().getTitle(), 
            event.getNotification().getMessage(), 
            event.getNotification().getCallback_for_accept(), 
            event.getNotification().getCallback_for_decline(),
            event.getNotification().getPlayer().getId()
        );
        notificationSink.tryEmitNext(dto);
    }
}

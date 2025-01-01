package com.new_era.alpha.events.dto;

public record NotificationStreamDTO(
    String title,
    String message,
    String callback_for_accept,
    String callback_for_decline,
    Integer player_id
) {
    
}

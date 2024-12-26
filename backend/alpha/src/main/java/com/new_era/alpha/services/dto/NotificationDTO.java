package com.new_era.alpha.services.dto;

public record NotificationDTO(
    Integer target_player_id,
    String title,
    String message,
    String icon,
    String href, 
    String callback_for_accept,
    String callback_for_decline
) {}

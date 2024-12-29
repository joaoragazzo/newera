package com.new_era.alpha.controllers.dto;

public record NotificationDTO(
    String title,
    String message,
    String callback_for_accept,
    String callback_for_decline
) {
    
}

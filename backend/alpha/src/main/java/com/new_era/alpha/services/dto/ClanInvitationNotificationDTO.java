package com.new_era.alpha.services.dto;

public record ClanInvitationNotificationDTO(
    Integer player_id,
    Integer invite_id,
    String clan_tag,
    String clan_name,
    String inviter_name
) {
    
}

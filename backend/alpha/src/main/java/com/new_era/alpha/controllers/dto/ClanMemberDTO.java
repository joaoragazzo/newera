package com.new_era.alpha.controllers.dto;

import java.time.LocalDateTime;

import com.new_era.alpha.entities.enums.ClanRole;

public record ClanMemberDTO(
    String nick,
    Integer kills,
    Integer deaths,
    LocalDateTime last_seen_at,
    ClanRole role
) {
    
}

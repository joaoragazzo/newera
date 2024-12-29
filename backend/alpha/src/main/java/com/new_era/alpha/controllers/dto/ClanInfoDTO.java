package com.new_era.alpha.controllers.dto;

import java.util.List;

import com.new_era.alpha.entities.enums.ClanRole;

public record ClanInfoDTO(
    String name,
    String tag,
    String color,
    ClanRole role,
    List<ClanMemberDTO> members
) {
    
}

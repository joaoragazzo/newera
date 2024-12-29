package com.new_era.alpha.controllers.dto;

import java.util.List;

public record ClanInfoDTO(
    String name,
    String tag,
    String color,
    List<ClanMemberDTO> members
) {
    
}

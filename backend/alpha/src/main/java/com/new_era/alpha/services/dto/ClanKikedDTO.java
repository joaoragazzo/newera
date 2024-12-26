package com.new_era.alpha.services.dto;

public record ClanKikedDTO(
    String initiator_name,
    Integer target_id,
    String clan_tag,
    String clan_name
) {}

package com.new_era.alpha.controllers.dto;

public record FullItemInfoDTO(
    String key,
    String item_name,
    String category,
    String type,
    String tag,
    Float price
) {}

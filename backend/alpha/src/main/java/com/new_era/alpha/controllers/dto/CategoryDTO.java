package com.new_era.alpha.controllers.dto;

import java.util.List;

public record CategoryDTO(
    String value,
    String label,
    Boolean isLeaf,
    List<SubcategoryDTO> children
) {}

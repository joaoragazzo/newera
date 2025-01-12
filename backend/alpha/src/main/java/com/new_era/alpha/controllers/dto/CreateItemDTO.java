package com.new_era.alpha.controllers.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateItemDTO(
    @NotNull String name,
    @NotNull List<String> category,
    @Positive @NotNull Float price,
    @NotNull String type,
    @NotNull String description,
    @NotNull String thumbnail,
    List<String> images,
    List<String> youtubeLinks,
    List<String> tags,
    Integer stock,
    Boolean notifyPublicDiscord,
    Boolean notifyPrivateDiscord,
    Boolean notifySite,
    Boolean allowComments,
    Boolean allowRating,
    Boolean showAcquisition,
    Boolean manualDelivery
    
) {}

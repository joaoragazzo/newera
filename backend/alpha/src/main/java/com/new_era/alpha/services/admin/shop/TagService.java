package com.new_era.alpha.services.admin.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.shop.Tag;
import com.new_era.alpha.repositories.shop.TagRepository;
import com.new_era.alpha.services.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {
    
    private final TagRepository tagRepository;

    public Tag getTag(String tag_name) {
        return tagRepository.findTagByName(tag_name).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessages.INVALID_CLAN_TAG)
        );
    }

    public List<Tag> getTagsFromList(List<String> tags) {
        if (Objects.isNull(tags)) { // temp for debug, delete after
            return new ArrayList<>();
        }

        List<Tag> tags_obj = new ArrayList<>();

        for (String tagname : tags) {
            Tag tag  = getTag(tagname);
            tags_obj.add(tag);
        }

        return tags_obj;

    }

}

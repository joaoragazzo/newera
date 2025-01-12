package com.new_era.alpha.services.admin.shop;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.new_era.alpha.controllers.dto.CreateItemDTO;
import com.new_era.alpha.entities.enums.ItemTypes;
import com.new_era.alpha.entities.shop.Category;
import com.new_era.alpha.entities.shop.Item;
import com.new_era.alpha.entities.shop.Subcategory;
import com.new_era.alpha.entities.shop.Tag;
import com.new_era.alpha.repositories.shop.ItemRepository;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.player.NotificationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final NotificationService notificationService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private final TagService tagService;

    private Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAllActiveItens() {
        return itemRepository.findAllActiveItens();
    }

    public Item addNewItemToShop(CreateItemDTO payload) {

        validatePayload(payload);
        validateItemType(payload.type());

        String category_name = payload.category().get(0);

        Subcategory subcategory = null;
        if (payload.category().size() > 1) {
            String subcategory_name = payload.category().get(1);
            subcategory = subcategoryService.getSubcategory(subcategory_name);
        }

        Category category = categoryService.getCategory(category_name);
        List<Tag> tags = tagService.getTagsFromList(payload.tags());

        ItemTypes type = ItemTypes.valueOf(payload.type());

        Item item = buildItem(payload, category, subcategory, type, tags);
        
        return saveItem(item);

    }

    private Item buildItem(CreateItemDTO itemDTO, Category category, Subcategory subcategory, ItemTypes type, List<Tag> tags) {

        Item item = new Item();

        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setPrice(itemDTO.price());
        item.setThumbnail(itemDTO.thumbnail());
        item.setCategory(category);
        

        if (!Objects.isNull(subcategory))
            item.setSubcategory(subcategory);

        item.setImages(itemDTO.images());
        item.setTags(tags);
        item.setType(type);
        item.setAllow_comments(itemDTO.allowComments());
        item.setAllow_rating(itemDTO.allowRating());
        item.setNotify_private_discord(itemDTO.notifyPrivateDiscord());
        item.setNotify_public_discord(itemDTO.notifyPublicDiscord());
        item.setNotify_site(itemDTO.notifySite());
        item.setManual_delivery(itemDTO.manualDelivery());
        item.setShow_acquisitions(itemDTO.showAcquisition());
        item.setYoutube_links(itemDTO.youtubeLinks());

        item.setCreated_at(LocalDateTime.now());

        return item;
    }

    private void validatePayload(CreateItemDTO payload) {
        if (payload.name().length() > 64)
            throw new IllegalArgumentException(ErrorMessages.ITEM_NAME_TOO_LONG);

        if (payload.description().length() > 100)
            throw new IllegalArgumentException(ErrorMessages.ITEM_DESCRIPTION_TOO_LONG);
    }

    private void validateItemType(String type) {
        boolean isValid = EnumSet.allOf(ItemTypes.class).stream()
                .anyMatch(itemType -> itemType.name().equalsIgnoreCase(type));

        if (!isValid)
            throw new IllegalArgumentException("Invalid item type: " + type);

    }
}

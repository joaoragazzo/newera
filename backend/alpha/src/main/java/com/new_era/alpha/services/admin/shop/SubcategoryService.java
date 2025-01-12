package com.new_era.alpha.services.admin.shop;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.shop.Subcategory;
import com.new_era.alpha.repositories.shop.SubcategoryRepository;
import com.new_era.alpha.services.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubcategoryService {
    
    private final SubcategoryRepository subcategoryRepository;

    public Subcategory getSubcategory(String subcategory_name) {
        return subcategoryRepository.findCategoryByName(subcategory_name).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessages.INVALID_SUBCATEGORY_NAME)
        );
    }
}

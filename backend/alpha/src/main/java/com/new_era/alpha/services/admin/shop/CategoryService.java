package com.new_era.alpha.services.admin.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.shop.Category;
import com.new_era.alpha.repositories.shop.CategoryRepository;
import com.new_era.alpha.services.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public Category addNewCategory(String name) {
        Category category = new Category();
        category.setCreated_at(LocalDateTime.now());
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> findAllActiveCategories() {
        return categoryRepository.findAllActiveCategories();
    }

    public Category getCategory(String category_name) {
        return categoryRepository.findCategoryByName(category_name).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessages.INVALID_CATEGORY_NAME)
        );
    }

}

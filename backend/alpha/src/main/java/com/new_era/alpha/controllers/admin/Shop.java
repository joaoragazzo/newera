package com.new_era.alpha.controllers.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.controllers.dto.CategoryDTO;
import com.new_era.alpha.controllers.dto.CreateItemDTO;
import com.new_era.alpha.controllers.dto.FullItemInfoDTO;
import com.new_era.alpha.entities.shop.Category;
import com.new_era.alpha.entities.shop.Item;
import com.new_era.alpha.security.UserSession;
import com.new_era.alpha.services.admin.shop.CategoryService;
import com.new_era.alpha.services.admin.shop.ItemService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/shop")
public class Shop {
    
    private ItemService itemService;
    private CategoryService categoryService;
    private UserSession session;

    @PostMapping("/create/item")
    public ResponseEntity<Map<String, String>> createNewItem(@RequestBody CreateItemDTO payload) {        

        itemService.addNewItemToShop(payload);

        Map<String, String> response = new HashMap<>();
        response.put("success", "O item foi criado com sucesso!");

        return new ResponseEntity<>(response, HttpStatus.OK);
        
    }

    @PostMapping("/fetch/item")
    public List<FullItemInfoDTO> fetchItems() {

        List<Item> itens = itemService.getAllActiveItens();
        List<FullItemInfoDTO> response = new ArrayList<>();

        for (Item i : itens) {
            String key = i.getId().toString();

            FullItemInfoDTO itemInfoDTO = new FullItemInfoDTO(
                key, i.getName(), i.getCategory().getName(), i.getType().toString(), "", i.getPrice()
            );

            response.add(itemInfoDTO);
        }

        return response;

    }

    @GetMapping("/create/category") // change to post 
    public ResponseEntity<Map<String, String>> createNewCategory() {

        categoryService.addNewCategory("ARMAS"); // for debug propurses

        Map<String, String> response = new HashMap<>();
        response.put("success", "A categoria foi criada com sucesso!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/fetch/category")
    public List<CategoryDTO> fetchCategories() {

        List<Category> categories = categoryService.findAllActiveCategories();
        List<CategoryDTO> response = new ArrayList<>();

        for (Category c : categories) {
            CategoryDTO categoryDTO = new CategoryDTO(c.getName(), c.getName(), false, new ArrayList<>());
            response.add(categoryDTO);
        }

        return response;
    }
}

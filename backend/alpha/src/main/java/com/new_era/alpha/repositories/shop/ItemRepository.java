package com.new_era.alpha.repositories.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.new_era.alpha.entities.shop.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
    
    @Query("SELECT i FROM Item i WHERE i.deleted_at IS NULL")
    List<Item> findAllActiveItens();
}

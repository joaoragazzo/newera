package com.new_era.alpha.repositories.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.shop.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.deleted_at IS NULL")
    Optional<Category> findCategoryByName(@Param("name") String name);

    @Query("SELECT c FROM Category c WHERE c.deleted_at IS NULL")
    List<Category> findAllActiveCategories();
}

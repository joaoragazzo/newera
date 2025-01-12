package com.new_era.alpha.repositories.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.new_era.alpha.entities.shop.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
    

    @Query("SELECT sc FROM Subcategory sc WHERE sc.name = :name AND sc.deleted_at IS NULL")
    Optional<Subcategory> findCategoryByName(@Param("name") String name);

}

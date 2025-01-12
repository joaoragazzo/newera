package com.new_era.alpha.repositories.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.new_era.alpha.entities.shop.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>{
    
    @Query("SELECT t FROM Tag t WHERE t.tag = :tag AND t.deleted_at IS NULL")
    Optional<Tag> findTagByName(@Param("tag") String tag);
}

package com.new_era.alpha.repositories.clan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.clan.Clan;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Integer> {
    
    @Query("SELECT c FROM Clan c WHERE c.deleted_at IS NULL")
    List<Clan> findAllActiveClans();
}

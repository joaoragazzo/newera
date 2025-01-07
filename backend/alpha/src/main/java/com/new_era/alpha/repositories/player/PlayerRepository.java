package com.new_era.alpha.repositories.player;

import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.player.Player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    
    @Query("SELECT p FROM Player p WHERE p.id NOT IN (SELECT cf.player.id FROM ClanFiliation cf WHERE cf.left_at IS NULL)")
    List<Player> getUnaffiliatedPlayers();
    
}

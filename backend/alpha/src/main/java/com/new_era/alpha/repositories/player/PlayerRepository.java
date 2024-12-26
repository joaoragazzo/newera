package com.new_era.alpha.repositories.player;

import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.player.Player;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    
}

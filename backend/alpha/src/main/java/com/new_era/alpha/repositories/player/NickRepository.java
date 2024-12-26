package com.new_era.alpha.repositories.player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.player.Nick;

@Repository
public interface NickRepository extends JpaRepository<Nick, Integer> {
    
    @Query("SELECT n FROM Nick n WHERE n.player.id = :playerId ORDER BY n.created_at DESC")
    Optional<Nick> findMostRecentNickByPlayerId(@Param("playerId") Integer playerId);

}

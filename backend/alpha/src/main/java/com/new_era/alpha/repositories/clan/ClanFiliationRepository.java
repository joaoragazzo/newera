package com.new_era.alpha.repositories.clan;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.entities.player.Player;

@Repository
public interface ClanFiliationRepository extends JpaRepository<ClanFiliation, Integer> {
    
    @Query("SELECT cf.player FROM ClanFiliation cf WHERE cf.clan.id = :clanId")
    List<Player> findPlayersHistoryByClanId(@Param("clanId") Integer clanId);

    @Query("SELECT cf.player FROM ClanFiliation cf WHERE cf.clan.id = :clanId AND cf.left_at IS NULL")
    List<Player> findActivePlayersByClanId(@Param("clanId") Integer clanId);

    @Query("SELECT cf FROM ClanFiliation cf WHERE cf.clan.id = :clanId AND cf.left_at IS NULL")
    List<ClanFiliation> findAllActiveClanFiliationByClanId(@Param("clanId") Integer clanId);

    @Query("SELECT cf FROM ClanFiliation cf WHERE cf.player.id = :player_id AND cf.left_at IS NULL")
    Optional<ClanFiliation> findActiveClanByPlayerId(@Param("player_id") Integer player_id);
}

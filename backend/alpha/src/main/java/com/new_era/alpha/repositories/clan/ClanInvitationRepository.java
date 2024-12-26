package com.new_era.alpha.repositories.clan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.new_era.alpha.entities.clan.ClanInvitation;

@Repository
public interface ClanInvitationRepository extends JpaRepository<ClanInvitation, Integer> {
    
    @Query("SELECT ci FROM ClanInvitation ci WHERE ci.expire_at > CURRENT_TIMESTAMP AND ci.player.id = :playerId AND ci.clan.id = :clanId")
    List<ClanInvitation> findAllActiveInvitesFromAClanToPlayer(@Param("playerId") Integer playerId, @Param("clanId") Integer clanId);

}

package com.new_era.alpha.services.clan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.entities.enums.ClanRole;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClanManagementService {
    
    private final ClanFiliationService clanFiliationService;
    private final ClanService clanService;
    private final PlayerService playerService;

    public Clan createClanWithOwner(String name, String tag, String color, Integer owner_id) {

        if (clanFiliationService.checkPlayerInAnyClan(owner_id))
            throw new IllegalArgumentException(String.format(ErrorMessages.PLAYER_IN_A_CLAN, owner_id));

        Clan clan = clanService.createClan(tag, name, color);
        Player player = playerService.getPlayerById(owner_id); 

        clanFiliationService.generateNewFiliation(player.getId(), clan.getId());
        clanFiliationService.promoteToOwner(owner_id);

        return clan;
    }

    public void deleteClanWithOwner(Integer initiator_id) {

        ClanFiliation initiator_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(initiator_id);

        if ( initiator_filiation.getRole() != ClanRole.OWNER ) 
            throw new IllegalArgumentException(ErrorMessages.PLAYER_IS_NOT_OWNER_FROM_CLAN);

        Clan clan = initiator_filiation.getClan(); 
            
        List<ClanFiliation> filiations = clanFiliationService.getAllActiveClanFiliationsByClanId(clan.getId());

        for (ClanFiliation filiation : filiations ) {
            clanFiliationService.forcedFinishClanFiliation(filiation.getId());
        }
    }

    public Clan changeClanName(Integer initiator_id, Integer clan_id, String new_name) {
        Clan clan = clanService.getClanById(clan_id);
        ClanFiliation initiator_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(initiator_id);

        if (initiator_filiation.getRole() != ClanRole.OWNER)
            throw new IllegalArgumentException(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION);
        
        if (clan.getId() != initiator_filiation.getClan().getId()) 
            throw new IllegalArgumentException(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN);

        return clanService.changeName(clan_id, new_name);
        
    } 

    public Clan changeClanTag(Integer initiator_id, Integer clan_id, String new_tag) {
        Clan clan = clanService.getClanById(clan_id);
        ClanFiliation initiator_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(initiator_id);

        if (initiator_filiation.getRole() != ClanRole.OWNER)
            throw new IllegalArgumentException(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION);
        
        if (clan.getId() != initiator_filiation.getClan().getId()) 
            throw new IllegalArgumentException(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN);

        return clanService.changeTag(clan_id, new_tag);
        
    }

    public Clan changeClanColor(Integer initiator_id, Integer clan_id, String new_color) {
        Clan clan = clanService.getClanById(clan_id);
        ClanFiliation initiator_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(initiator_id);

        if (initiator_filiation.getRole() != ClanRole.OWNER)
            throw new IllegalArgumentException(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION);
        
        if (clan.getId() != initiator_filiation.getClan().getId()) 
            throw new IllegalArgumentException(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN);

        return clanService.changeColor(clan_id, new_color);
        
    }
}

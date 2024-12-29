package com.new_era.alpha.services.clan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.entities.enums.ClanRole;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.clan.ClanFiliationRepository;
import com.new_era.alpha.services.dto.ClanKikedDTO;
import com.new_era.alpha.services.enums.ClanActions;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.player.NotificationService;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClanFiliationService {

    private final ClanFiliationRepository clanFiliationRepository;

    private final PlayerService playerService;
    private final ClanService clanService;
    private final NotificationService notificationService;


    public boolean kickPlayerFromClan(Integer initiator_id, Integer target_id) {

        Clan clan = getPlayerClanByPlayerId(initiator_id);

        bothPlayerAreInTheSameClan(initiator_id, target_id);
        checkIfPlayerCanKick(initiator_id);

        ClanFiliation initiator_filiation = getPlayerFilitiationByPlayerId(initiator_id);
        ClanFiliation target_filiation = getPlayerFilitiationByPlayerId(target_id);

        if (initiator_filiation.getRole() == ClanRole.ADMIN && 
            target_filiation.getRole() == ClanRole.ADMIN) {
                throw new IllegalArgumentException(ErrorMessages.ADMIN_CANNOT_KICK_ANOTHER_ADMIN);
        }
        
        target_filiation.setLeft_at(LocalDateTime.now());
        clanFiliationRepository.save(target_filiation);

        String initiator_name = playerService.getLastNick(initiator_id);

        ClanKikedDTO notification = new ClanKikedDTO(initiator_name, target_id, clan.getTag(), clan.getName());
        notificationService.sendKickedFromClanNotification(notification);
        
        return true;
    }

    public boolean exitFromClan(Integer player_id) {

        ClanFiliation filiation = getPlayerFilitiationByPlayerId(player_id);

        if (filiation.getRole() == ClanRole.OWNER) 
            throw new IllegalArgumentException(ErrorMessages.OWNER_CANNOT_LEAVE_CLAN);
        

        filiation.setLeft_at(LocalDateTime.now());
        clanFiliationRepository.save(filiation);
        return true;
    }

    public void forcedFinishClanFiliation(Integer clan_filiation_id) {
        ClanFiliation clan_filiation = getClanFiliation(clan_filiation_id);
        clan_filiation.setLeft_at(LocalDateTime.now());
        clanFiliationRepository.save(clan_filiation);
    }

    public List<Player> getAllPlayersByClanId(Integer clan_id) {
        List<Player> players = clanFiliationRepository.findActivePlayersByClanId(clan_id);
        return players;
    }

    public List<ClanFiliation> getAllActiveClanFiliationsByClanId(Integer clan_id) {
        List<ClanFiliation> filiations = clanFiliationRepository.findAllActiveClanFiliationByClanId(clan_id);
        return filiations;
    }

    public Clan getPlayerClanByPlayerId(Integer player_id) {

        ClanFiliation filiation = clanFiliationRepository.findActiveClanByPlayerId(player_id).orElseThrow(
                () -> new IllegalArgumentException(String.format(ErrorMessages.PLAYER_NOT_IN_A_CLAN, player_id)));

        if (!Objects.isNull(filiation.getClan().getDeleted_at())) 
            throw new IllegalArgumentException(ErrorMessages.CLAN_HAS_BEEN_ALREADY_DELETED);

        return filiation.getClan();
    }

    public ClanFiliation getPlayerFilitiationByPlayerId(Integer player_id) throws IllegalArgumentException {
        ClanFiliation filiation = clanFiliationRepository.findActiveClanByPlayerId(player_id).orElseThrow(
                () -> new IllegalArgumentException(String.format(ErrorMessages.PLAYER_NOT_IN_A_CLAN, player_id)));

        return filiation;
    }

    public ClanFiliation generateNewFiliation(Integer player_id, Integer clan_id) {
        Player player = playerService.getPlayerById(player_id);
        Clan clan = clanService.getClanById(clan_id);
     
        LocalDateTime now = LocalDateTime.now();

        ClanFiliation clan_filiation = new ClanFiliation();
        clan_filiation.setClan(clan);
        clan_filiation.setJoined_at(now);
        clan_filiation.setRole(ClanRole.MEMBER);
        clan_filiation.setPlayer(player);
        return clanFiliationRepository.save(clan_filiation);
    }

    public boolean checkPlayerInAnyClan(Integer player_id) {
        return clanFiliationRepository.findActiveClanByPlayerId(player_id).isPresent();
    }

    private boolean canPerformAction(Integer player_id, ClanActions action) {

        ClanFiliation filiation = getPlayerFilitiationByPlayerId(player_id);
        ClanRole role = filiation.getRole();

        return switch (role) {
            case OWNER -> true;
            case ADMIN -> action == ClanActions.INVITE;
            case MEMBER -> false;
        };
    }

    private void checkIfPlayerCanKick(Integer player_id) {
        if (!canPerformAction(player_id, ClanActions.KICK)) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.MEMBER_DOES_NOT_HAVE_KICK_PERMISSION, player_id));
        }
    }

    public void checkIfPlayerCanInvite(Integer player_id) {
        if (!canPerformAction(player_id, ClanActions.INVITE)) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.MEMBER_DOES_NOT_HAVE_INVITE_PERMISSION, player_id));
        }
    }

    public void checkIfPlayerCanPromote(Integer player_id) {
        if (!canPerformAction(player_id, ClanActions.PROMOTE)) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.MEMBER_DOES_NOT_HAVE_PROMOTE_PERMISSION, player_id));
        }
    }

    public void checkIfPlayerCanDemote(Integer player_id) {
        if (!canPerformAction(player_id, ClanActions.DEMOTE)) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.MEMBER_DOES_NOT_HAVE_DEMOTE_PERMISSION, player_id));
        }
    }

    public ClanFiliation getClanFiliation(Integer clan_filiation_id) {
        return clanFiliationRepository.findById(clan_filiation_id).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessages.CLAN_FILIATION_DOES_NOT_EXISTS)
        );
    }

    public ClanFiliation promotePlayer(Integer initiator_id, Integer target_id) {
        bothPlayerAreInTheSameClan(initiator_id, target_id);
        checkIfPlayerCanPromote(initiator_id);
        
        ClanFiliation filiation = getPlayerFilitiationByPlayerId(target_id);

        if (filiation.getRole() != ClanRole.MEMBER)
            throw new IllegalArgumentException(ErrorMessages.PLAYER_CANNOT_BE_PROMOTED);

        filiation.setRole(ClanRole.ADMIN);
        return clanFiliationRepository.save(filiation);
        
    }

    public ClanFiliation demotePlayer(Integer initiator_id, Integer target_id) {
        bothPlayerAreInTheSameClan(initiator_id, target_id);
        checkIfPlayerCanDemote(initiator_id);
        
        ClanFiliation filiation = getPlayerFilitiationByPlayerId(target_id);

        if (filiation.getRole() != ClanRole.ADMIN)
            throw new IllegalArgumentException(ErrorMessages.PLAYER_CANNOT_BE_DEMOTED);

        filiation.setRole(ClanRole.MEMBER);
        return clanFiliationRepository.save(filiation);
        
    }

    public ClanFiliation promoteToOwner(Integer player_id) {
        ClanFiliation clanFiliation = getPlayerFilitiationByPlayerId(player_id);
        clanFiliation.setRole(ClanRole.OWNER);
        return clanFiliationRepository.save(clanFiliation);
    }

    private void bothPlayerAreInTheSameClan(Integer player_1, Integer player_2) {
        Clan initiator_clan = getPlayerClanByPlayerId(player_1);
        Clan target_clan = getPlayerClanByPlayerId(player_2);

        if (initiator_clan.getId() != target_clan.getId()) 
            throw new IllegalArgumentException(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN);
    }

}



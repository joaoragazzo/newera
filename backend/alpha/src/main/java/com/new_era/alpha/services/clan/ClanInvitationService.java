package com.new_era.alpha.services.clan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanInvitation;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.clan.ClanInvitationRepository;
import com.new_era.alpha.services.dto.ClanInvitationNotificationDTO;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.player.NotificationService;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClanInvitationService {
    
    private final ClanInvitationRepository clanInvitationRepository;

    private final PlayerService playerService;
    private final ClanFiliationService clanFiliationService;
    private final ClanService clanService;
    private final NotificationService notificationService;

    public Integer generateInvite(Integer player_id, Integer clan_id) {
        Clan clan = clanService.getClanById(clan_id);
        Player player = playerService.getPlayerById(player_id);

        LocalDateTime now = LocalDateTime.now();

        ClanInvitation invitation = new ClanInvitation();

        invitation.setClan(clan);
        invitation.setPlayer(player);
        invitation.setCreated_at(now);
        invitation.setExpire_at(now.plusWeeks(1));
        invitation.setAccepted(Boolean.FALSE);
        
        ClanInvitation savedClanInvitation = clanInvitationRepository.save(invitation);
        return savedClanInvitation.getId();
    
    }

    public ClanInvitation getInvitateById(Integer invite_id) {
        return clanInvitationRepository.findById(invite_id).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessages.CLAN_INVITE_DOES_NOT_EXITS)
        );
    }
    
    public boolean checkIfPlayerAlreadyHaveAValidInviteFromAClan(Integer player_id, Integer clan_id) {
        List<ClanInvitation> invitations = clanInvitationRepository.findAllActiveInvitesFromAClanToPlayer(player_id, clan_id);
        return invitations.size() > 0;
    };

    public void acceptInviteToClan(Integer player_id, Integer invite_id) {

        Player player = playerService.getPlayerById(player_id);
        
        if (clanFiliationService.checkPlayerInAnyClan(player_id))
            throw new IllegalArgumentException(String.format(ErrorMessages.PLAYER_IN_A_CLAN, player_id));

        ClanInvitation invitation = getInvitateById(invite_id);

        Clan clan = invitation.getClan();

        LocalDateTime now = LocalDateTime.now();

        if (!Objects.equals(player, invitation.getPlayer()))
            throw new IllegalArgumentException(ErrorMessages.INVITE_IS_NOT_TO_THIS_PLAYER);
        
        if (now.isAfter(invitation.getExpire_at()))
            throw new IllegalArgumentException(ErrorMessages.INVITE_IS_ALREADY_EXPIRED);

        if (invitation.getAccepted())
            throw new IllegalArgumentException(ErrorMessages.INVITE_IS_ALREADY_ACCEPTED);

        if (invitation.getDeclined())
            throw new IllegalArgumentException(ErrorMessages.INVITE_IS_ALREADY_ACCEPTED);

        if (clan.isClosed())
            throw new IllegalArgumentException(ErrorMessages.CLAN_HAS_BEEN_ALREADY_DELETED); 

        clanFiliationService.generateNewFiliation(player_id, clan.getId());

        invitation.setAccepted(true);
        invitation.setAccepted_at(now);
        clanInvitationRepository.save(invitation);
    
    }

    public ClanInvitation declineInviteToClan(Integer player_id, Integer invite_id) {
        Player player = playerService.getPlayerById(player_id);

        ClanInvitation invitation = getInvitateById(invite_id);

        if (invitation.getPlayer().getId() != player.getId())
            throw new IllegalArgumentException(ErrorMessages.INVITE_IS_NOT_TO_THIS_PLAYER);
        
        invitation.setDeclined(true);
        invitation.setDeclined_at(LocalDateTime.now());
        return clanInvitationRepository.save(invitation);
    }

    public boolean invitePlayerToClan(Integer inviter_id, Integer invitee_id) throws IllegalArgumentException {

        Player inviter = playerService.getPlayerById(inviter_id);
        Player invitee = playerService.getPlayerById(invitee_id);

        Clan clan = clanFiliationService.getPlayerClanByPlayerId(inviter.getId());

        if (checkIfPlayerAlreadyHaveAValidInviteFromAClan(invitee_id, clan.getId())) 
            throw new IllegalArgumentException(ErrorMessages.INVITE_ALREADY_EXISTS_FOR_TARGET);
        
        if (clanFiliationService.checkPlayerInAnyClan(invitee_id)) 
            throw new IllegalArgumentException(String.format(ErrorMessages.PLAYER_IN_A_CLAN, invitee_id));

        clanFiliationService.checkIfPlayerCanInvite(inviter.getId());

        String inviterLastNick = playerService.getLastNick(inviter.getId());

        Integer clanInvitationId = generateInvite(invitee.getId(), clan.getId());

        ClanInvitationNotificationDTO notification = new ClanInvitationNotificationDTO(
                invitee.getId(), clanInvitationId, clan.getTag(), clan.getName(), inviterLastNick);

        notificationService.sendClanInviteNotification(notification);
        return true;

    }

    
}

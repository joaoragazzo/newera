package com.new_era.alpha;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.entities.clan.ClanInvitation;
import com.new_era.alpha.entities.enums.ClanRole;
import com.new_era.alpha.entities.player.Nick;
import com.new_era.alpha.entities.player.Notification;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.clan.ClanFiliationRepository;
import com.new_era.alpha.repositories.clan.ClanInvitationRepository;
import com.new_era.alpha.repositories.clan.ClanRepository;
import com.new_era.alpha.repositories.player.NickRepository;
import com.new_era.alpha.repositories.player.NotificationRepository;
import com.new_era.alpha.repositories.player.PlayerRepository;
import com.new_era.alpha.services.clan.ClanFiliationService;
import com.new_era.alpha.services.clan.ClanInvitationService;
import com.new_era.alpha.services.clan.ClanManagementService;
import com.new_era.alpha.services.clan.ClanService;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.messages.player.NotificationMessages;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class TestClanFiliation {

    Random randomGenerator = new Random();
    @Autowired
    ClanRepository clanRepository;
    
    @Autowired
    PlayerRepository playerRepository;
    
    @Autowired
    ClanFiliationRepository clanFiliationRepository;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    NickRepository nickRepository;

    @Autowired
    ClanFiliationService clanFiliationService;

    @Autowired
    ClanInvitationRepository clanInvitationRepository;

    @Autowired
    ClanInvitationService clanInvitationService;

    @Autowired
    ClanManagementService clanManagementService;

    @Autowired
    ClanService clanService;

	private Clan create_clan(String name, String tag) {
		Clan clan = new Clan();
        clan.setCreated_at(LocalDateTime.now().minusDays(1));
        clan.setName(name);
        clan.setTag(tag);
        clan.setColor("#000000");
        return clanRepository.save(clan);
	}

    private ClanFiliation create_clan_filiation(Clan clan, Player player, ClanRole role) {
        ClanFiliation filiation = new ClanFiliation();
        
        filiation.setClan(clan);
        filiation.setPlayer(player);
        filiation.setRole(role);
        filiation.setJoined_at(LocalDateTime.now());

        return clanFiliationRepository.save(filiation);
    }

	private Player create_player() {
		Player player = new Player();
        BigInteger random_steam64id = BigInteger.valueOf(randomGenerator.nextInt());
		player.setSteam64id(random_steam64id);
		return playerRepository.save(player);
	}

    private Nick create_nick(Player player, String nickname) {
        Nick nick = new Nick();
        nick.setCreated_at(LocalDateTime.now().minusDays(1));
        nick.setName(nickname);
        nick.setPlayer(player);
        return nickRepository.save(nick);
    }

    private ClanInvitation create_valid_invite(Player player, Clan clan) {
        ClanInvitation clan_invitation = new ClanInvitation();
        clan_invitation.setPlayer(player);
        clan_invitation.setClan(clan);
        clan_invitation.setCreated_at(LocalDateTime.now());
        clan_invitation.setExpire_at(LocalDateTime.now().plusDays(3));
        return clanInvitationRepository.save(clan_invitation);
    }

    private ClanInvitation create_invalid_invite(Player player, Clan clan) {
        ClanInvitation clan_invitation = new ClanInvitation();
        clan_invitation.setPlayer(player);
        clan_invitation.setClan(clan);
        clan_invitation.setCreated_at(LocalDateTime.now().minusDays(10));
        clan_invitation.setExpire_at(LocalDateTime.now().minusDays(7));
        return clanInvitationRepository.save(clan_invitation);
    }


	@Test
	@DisplayName("#findPlayersByClanId > When use the function passing clan id > Return all players")
	void findPlayersByClanIdWhenUseTheFunctionPassingClanIdReturnAllPlayers() {
        Player player1 = create_player();
        Player player2 = create_player();
        Player player3 = create_player();
        Player player4 = create_player();


        Clan clan = create_clan("Test", "TEST");
        create_clan_filiation(clan, player1, ClanRole.OWNER);
        create_clan_filiation(clan, player2, ClanRole.ADMIN);
        create_clan_filiation(clan, player3, ClanRole.MEMBER);
        ClanFiliation old_filiation = create_clan_filiation(clan, player4, ClanRole.MEMBER);
        old_filiation.setJoined_at(LocalDateTime.now().minusDays(3));
        old_filiation.setLeft_at(LocalDateTime.now().minusDays(1));
        clanFiliationRepository.save(old_filiation);


        List<Player> members = clanFiliationService.getAllPlayersByClanId(clan.getId());

        assertAll(
            () -> assertEquals(3, members.size()),
            () -> assertEquals(player1, members.get(0)),
            () -> assertEquals(player2, members.get(1)),
            () -> assertEquals(player3, members.get(2))
        );
        
    }

	@Test
	@DisplayName("#invitePlayerToClan > When inviter is OWNER > When invitee is not in a clan > Invite the valid player AND generate notification")
	void invitePlayerToClanWhenInviterIsOWNERWhenInveteeIsNotInAClanInviteValidPlayerANDGenerateNotification() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "Inviter");
        create_nick(player2, "Invitee");
        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.OWNER);

        clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId());
        
        List<Notification> notifications = notificationRepository.findAll();

        String expected_message = String.format(NotificationMessages.CLAN_INVITE_MESSAGE, "TEST", "TEST", "Inviter");
        Notification notification = notifications.get(0);

        List<ClanInvitation> invitations = clanInvitationRepository.findAll();
        ClanInvitation invitation = invitations.get(0);

        assertAll(
            () -> assertEquals(1, notifications.size()),
            () -> assertEquals(expected_message, notification.getMessage()),
            () -> assertEquals(NotificationMessages.CLAN_INVITE_TITLE, notification.getTitle()),
            () -> assertEquals(1, invitations.size()),
            () -> assertEquals(player2.getId(), invitation.getPlayer().getId()),
            () -> assertEquals(clan.getId(), invitation.getClan().getId())
        );

	}

	@Test
	@DisplayName("#invitePlayerToClan > When inviter is ADMIN > When invitee is not in a clan > Invite the valid player")
	void invitePlayerToClanWhenInviterIsADMINWhenInveteeIsNotInAClanInviteValidPlayer() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "Inviter");
        create_nick(player2, "Invitee");
        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.ADMIN);

        clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId());
        
        List<Notification> notifications = notificationRepository.findAll();

        String expected_message = String.format(NotificationMessages.CLAN_INVITE_MESSAGE, "TEST", "TEST", "Inviter");
        Notification notification = notifications.get(0);

        List<ClanInvitation> invitations = clanInvitationRepository.findAll();
        ClanInvitation invitation = invitations.get(0);

        assertAll(
            () -> assertEquals(1, notifications.size()),
            () -> assertEquals(expected_message, notification.getMessage()),
            () -> assertEquals(NotificationMessages.CLAN_INVITE_TITLE, notification.getTitle()),
            () -> assertEquals(1, invitations.size()),
            () -> assertEquals(player2.getId(), invitation.getPlayer().getId()),
            () -> assertEquals(clan.getId(), invitation.getClan().getId())
        );
	}

	@Test
	@DisplayName("#invitePlayerToClan > When inviter is MEMBER > When invitee is not in a clan > Throw error AND does not invite")
	void invitePlayerToClanWhenInviterIsMEMBERWhenInveteeIsNotInAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "Inviter");
        create_nick(player2, "Invitee");
        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.MEMBER);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId())
        );
        
        List<Notification> notifications = notificationRepository.findAll();
        List<ClanInvitation> invitations = clanInvitationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertEquals(0, invitations.size()),
            () -> assertEquals(
                ErrorMessages.MEMBER_DOES_NOT_HAVE_INVITE_PERMISSION
                , exception.getMessage())
        );
	}

	@Test
	@DisplayName("#invitePlayerToClan > When inviter is OWNER > When invitee is in a clan > Throw error")
	void invitePlayerToClanWhenWhenInviterIsOWNERWhenInveteeIsInAClanInviteValidPlay() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "Inviter");
        create_nick(player2, "Invitee");
        Clan clan1 = create_clan("TEST", "TEST");
        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan1, player1, ClanRole.OWNER);
        create_clan_filiation(clan2, player2, ClanRole.OWNER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId()) 
        );

        List<Notification> notifications = notificationRepository.findAll();
        List<ClanInvitation> invitations = clanInvitationRepository.findAll();
	
        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertEquals(0, invitations.size()),
            () -> assertEquals(
                ErrorMessages.PLAYER_IN_A_CLAN, 
                exception.getMessage())
        );


    }

	@Test
	@DisplayName("#invitePlayerToClan > When inviter is not in a clan > When invitee is not in a clan > Throw error")
	void invitePlayerToClanWhenInviterIsNotInAClanWhenInviteeIsNotInAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "Inviter");
        create_nick(player2, "Invitee");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId()) 
        );

        List<Notification> notifications = notificationRepository.findAll();
        List<ClanInvitation> invitations = clanInvitationRepository.findAll();
	
        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertEquals(0, invitations.size()),
            () -> assertEquals(
                    ErrorMessages.PLAYER_NOT_IN_A_CLAN
                    , exception.getMessage())
        );
	}


	@Test
	@DisplayName("#invitePlayerToClan > When inviter was in a clan > When invitee is not in a clan > Throw error")
	void invitePlayerToClanWhenInviterWasInAClanWhenInviteeIsNotInAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();

        create_nick(player1, "Ex clan member");
        create_nick(player2, "No clan member");
        
        Clan clan1 = create_clan("TEST", "TEST");
        ClanFiliation filiation = create_clan_filiation(clan1, player1, ClanRole.ADMIN);
    
        filiation.setLeft_at(LocalDateTime.now());
        clanFiliationRepository.save(filiation);
    
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanInvitationService.invitePlayerToClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();
        List<ClanInvitation> invitations = clanInvitationRepository.findAll();
	
        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertEquals(0, invitations.size()),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN, 
                exception.getMessage())
        );
	}

    @Test
    @DisplayName("#invitePlayerToClan > when inviter is owner > when invitee is not in a clan > when already has a invite > throw error")
    void invitePlayerToClanWhenInviterIsOwnerWhenInviteeIsNotInAClanWhenAlreadyHasAInviteThrowError() {
        Player owner = create_player();
        Player member = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_valid_invite(member, clan);

        IllegalArgumentException exception  = assertThrows(
            IllegalArgumentException.class,
            () -> clanInvitationService.invitePlayerToClan(owner.getId(), member.getId())
        );

        assertEquals(ErrorMessages.INVITE_ALREADY_EXISTS_FOR_TARGET, exception.getMessage());
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When the initiator is the owner > When target is member > kick the player")
    void kickPlayerFromClanWhenTheInitiatorIsTheOwnerWhenTargetIsMemberKickThePlayer() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "OWNER");
        create_nick(player2, "TARGET");

        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.OWNER);
        create_clan_filiation(clan, player2, ClanRole.MEMBER);

        assertDoesNotThrow(
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();
        Notification notification = notifications.get(0);
        
        String expected_message = String.format(NotificationMessages.CLAN_KIKED_MESSAGE, 
            clan.getTag(), clan.getName(), "OWNER"); 
        String expected_title = NotificationMessages.CLAN_KIKED_TITLE;


        assertAll(
            () -> assertEquals(1, notifications.size()),
            () -> assertEquals(expected_message, notification.getMessage()),
            () -> assertEquals(expected_title, notification.getTitle()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player2.getId()))
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is admin > When the target is in clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsAdminWhenTheTargetIsInClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "ADMIN");
        create_nick(player2, "TARGET");

        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.ADMIN);
        create_clan_filiation(clan, player2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.MEMBER_DOES_NOT_HAVE_KICK_PERMISSION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is member > When target is in clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsMemberWhenTargetIsInClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "MEMBER");
        create_nick(player2, "TARGET");

        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.MEMBER);
        create_clan_filiation(clan, player2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.MEMBER_DOES_NOT_HAVE_KICK_PERMISSION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is admin > When target is admin > throw error")
    void kickPlayerFromClanWhenInitiatorIsAdminWhenTargetIsAdminThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "ADMIN");
        create_nick(player2, "ADMIN");

        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.ADMIN);
        create_clan_filiation(clan, player2, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.MEMBER_DOES_NOT_HAVE_KICK_PERMISSION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is owner > When target is not in clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsOwnerWhenTargetIsNotInClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "OWNER");
        create_nick(player2, "NOT-A-MEMBER");

        Clan clan = create_clan("TEST", "TEST");
        create_clan_filiation(clan, player1, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN, 
                exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is owner from clan A > When target is member from clan B > throw error")
    void kickPlayerFromClanWhenInitiatorIsOwnerFromClanAWhenTargetIsMemberFromClanBThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "OWNER");
        create_nick(player2, "NOT-A-MEMBER");

        Clan clan1 = create_clan("TEST1", "TEST1");
        create_clan_filiation(clan1, player1, ClanRole.OWNER);

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is owner from clan A > When target is admin from clan B > throw error")
    void kickPlayerFromClanWhenInitiatorIsOwnerFromClanAWhenTargetIsAdminFromClanBThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "OWNER1");
        create_nick(player2, "ADMIN2");

        Clan clan1 = create_clan("TEST1", "TEST1");
        create_clan_filiation(clan1, player1, ClanRole.OWNER);

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is owner from clan A > When target is owner from clan B > throw error")
    void kickPlayerFromClanWhenInitiatorIsOwnerFromClanAWhenTargetIsOwnerFromClanBThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "OWNER1");
        create_nick(player2, "OWNER2");

        Clan clan1 = create_clan("TEST1", "TEST1");
        create_clan_filiation(clan1, player1, ClanRole.OWNER);

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.OWNER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is not in a clan > When target is member of a clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsNotInAClanWhenTargetIsMemberOfAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "NOT-A-MEMBER");
        create_nick(player2, "MEMBER");

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN, 
                exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is not in a clan > When target is admin of a clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsNotInAClanWhenTargetIsAdminOfAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "NOT-A-MEMBER");
        create_nick(player2, "MEMBER");

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN, 
                exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > When initiator is not in a clan > When target is owner of a clan > throw error")
    void kickPlayerFromClanWhenInitiatorIsNotInAClanWhenTargetIsOwnerOfAClanThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "NOT-A-MEMBER");
        create_nick(player2, "MEMBER");

        Clan clan2 = create_clan("TEST2", "TEST2");
        create_clan_filiation(clan2, player2, ClanRole.OWNER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN, 
                exception.getMessage())
        );
    }

    @Test
    @DisplayName("#kickPlayerFromClan > when initiator is not a member > when target is not a member > throw error")
    void kickPlayerFromClanWhenInitiatorIsNotAMemberWhenTargetIsNotAMemberThrowError() {
        Player player1 = create_player();
        Player player2 = create_player();
        create_nick(player1, "NOT-A-MEMBER");
        create_nick(player2, "MEMBER");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.kickPlayerFromClan(player1.getId(), player2.getId())
        );

        List<Notification> notifications = notificationRepository.findAll();

        assertAll(
            () -> assertEquals(0, notifications.size()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player2.getId())),
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN,
                exception.getMessage())
        );
    }

    @Test
    @DisplayName("#exitFromClan > When player is not a member > throw error")
    void exitFromClanWhenPlayerIsNotAMemberThrowError() {
        Player player1 = create_player();
        create_nick(player1, "MEMBER");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanFiliationService.exitFromClan(player1.getId())
        );

        assertAll(
            () -> assertEquals(ErrorMessages.PLAYER_NOT_IN_A_CLAN, exception.getMessage()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        );
    }

    @Test
    @DisplayName("#exitFromClan > When member is member > exit from clan")
    void exitFromClanWhenMemberIsMemberExitFromClan() {
        Player player1 = create_player();
        create_nick(player1, "MEMBER");

        Clan clan1 = create_clan("TESTE", "TESTE");
        create_clan_filiation(clan1, player1, ClanRole.MEMBER);

        assertAll(
            () -> assertDoesNotThrow(
                    () -> clanFiliationService.exitFromClan(player1.getId())
                ),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        );
    }

    @Test
    @DisplayName("#exitFromClan > When member is admin > exit from clan")
    void exitFromClanWhenMemberIsAdminExitFromClan() {
        Player player1 = create_player();
        create_nick(player1, "MEMBER");

        Clan clan1 = create_clan("TESTE", "TESTE");
        create_clan_filiation(clan1, player1, ClanRole.ADMIN);

        assertAll(
            () -> assertDoesNotThrow(
                    () -> clanFiliationService.exitFromClan(player1.getId())
                ),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        ); 
    }

    @Test
    @DisplayName("#exitFromClan > when member is owner > throw error")
    void exitFromClanWhenMemberIsOwnerThrowError() {
        Player player1 = create_player();
        create_nick(player1, "MEMBER");

        Clan clan1 = create_clan("TESTE", "TESTE");
        create_clan_filiation(clan1, player1, ClanRole.OWNER);

        IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> clanFiliationService.exitFromClan(player1.getId())
                );

        assertAll(
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player1.getId())),
            () -> assertEquals(ErrorMessages.OWNER_CANNOT_LEAVE_CLAN, exception.getMessage())
        ); 
    }

    @Test
    @DisplayName("#acceptInviteToClan > When the invite is valid > When invitee is not in a clan > When clan exists > invitee enter the clan")
    void acceptInviteToClanWhenTheInviteIsValidWhenInviteeIsNotInAClanWhenClanExistsInviteeEnterTheClan() {
        Player player = create_player();
        create_nick(player, "TEST");

        Player owner = create_player();
        create_nick(owner, "OWNER");

        Clan clan = create_clan("CLAN", "TAG");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        ClanInvitation invitation = create_valid_invite(player, clan);
        AtomicReference<ClanFiliation> possible_filiation = new AtomicReference<>();

        assertAll(
            () -> assertDoesNotThrow(
                () -> clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId()),
                "O jogador deve conseguir aceitar o convite"
            ),
            () -> assertDoesNotThrow(
                () -> {
                    possible_filiation.set(clanFiliationService.getPlayerFilitiationByPlayerId(player.getId()));
                },
                "O jogador deve ter uma filiação ao clan aceitado"
            )
        );

        ClanFiliation filiation = possible_filiation.get();
        Clan invited_player_clan = filiation.getClan();

        assertAll(
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player.getId()), 
                "O jogador deve pertencer ao clan"),
            () -> assertEquals(clan.getId(), invited_player_clan.getId(), 
                "O clan que ele pertence deve ser o mesmo que ele aceitou o convite")
        );
    }

    @Test
    @DisplayName("#acceptInviteToClan > When the invite is invalid > When invitee is not in a clan > When clan exists > invitee does not enter the clan")
    void acceptInviteToClanWhenTheInviteIsInvalidWhenInviteeIsNotInAClanWhenClanExistsInviteeDoesNotEnterTheClan() {
        Player player = create_player();
        create_nick(player, "TEST");

        Player owner = create_player();
        create_nick(owner, "OWNER");

        Clan clan = create_clan("CLAN", "TAG");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        ClanInvitation invitation = create_invalid_invite(player, clan);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId()),
                "O jogador não deve conseguir aceitar o convite"
            );

        assertAll(
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(player.getId()), 
                "O jogador não deve pertencer ao clan"),
            () -> assertEquals(ErrorMessages.INVITE_IS_ALREADY_EXPIRED, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#acceptInviteToClan > when the invitee is valid > when the invite is already accepted > when invitee was in the clan > throw error")
    void acceptInviteToClanWhenTheInviteeIsValidWhenTheInviteIsAlreadyAcceptedWhenInviteeWasInTheClanThrowError() {
        Player player = create_player();
        create_nick(player, "TEST");

        Player owner = create_player();
        create_nick(owner, "OWNER");

        Clan clan = create_clan("CLAN", "TAG");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        ClanInvitation invitation = create_valid_invite(player, clan);

        clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId());
        clanFiliationService.kickPlayerFromClan(owner.getId(), player.getId());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId()),
                "O jogador não deve conseguir aceitar o convite"
            );

        assertAll(
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(player.getId()), 
                "O jogador não deve pertencer ao clan"),
            () -> assertEquals(ErrorMessages.INVITE_IS_ALREADY_ACCEPTED, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#acceptInviteToClan > when invitee is valid > when invitee is valid > when clan was closed > throw error")
    void acceptInviteToClanWhenInviteeIsValidWhenInviteeIsValidWhenClanWasClosedThrowError() {
        Player player = create_player();
        create_nick(player, "TEST");

        Player owner = create_player();
        create_nick(owner, "OWNER");

        Clan clan = create_clan("CLAN", "TAG");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        ClanInvitation invitation = create_valid_invite(player, clan);

        clan.setDeleted_at(LocalDateTime.now());
        clanRepository.save(clan);
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId()),
                "O jogador não deve conseguir aceitar o convite"
            );

        assertAll(
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(player.getId()), 
                "O jogador não deve pertencer ao clan"),
            () -> assertEquals(ErrorMessages.CLAN_HAS_BEEN_ALREADY_DELETED, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#acceptInviteToClan > when invite is valid > when invitee is already in a clan > throw error")
    void acceptInviteToClanWhenInviteIsValidWhenInviteeIsAlreadyInAClanThrowError() {
        Player player = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, player, ClanRole.MEMBER);
        
        Clan another_clan = create_clan("another clan", "atag");
        
        ClanInvitation invitation = create_valid_invite(player, another_clan);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanInvitationService.acceptInviteToClan(player.getId(), invitation.getId())
        );

        ClanFiliation final_filiation = clanFiliationRepository.findActiveClanByPlayerId(player.getId()).get();
        Clan final_clan = final_filiation.getClan();

        assertAll(
            () -> assertEquals(ErrorMessages.PLAYER_IN_A_CLAN, exception.getMessage()),
            () -> assertEquals(final_clan.getId(), clan.getId())
        );
        
    }

    @Test
    @DisplayName("#createClanWithOwner > when owner is not in a clan > when all arguments are valid > create clan")
    void createClanWithOwnerWhenOwnerIsNotInAClanWhenAllArgumentsAreValidCreateClan() {
        Player player1 = create_player();
        
        Clan clan = clanManagementService.createClanWithOwner("name", "tag", "#000000", player1.getId());
        
        assertAll(
            () -> assertEquals(clan.getId(), clanFiliationService.getPlayerFilitiationByPlayerId(player1.getId()).getClan().getId()),
            () -> assertTrue(clanFiliationService.checkPlayerInAnyClan(player1.getId())),
            () -> assertEquals(1, clanFiliationService.getAllActiveClanFiliationsByClanId(clan.getId()).size())
        );
    }

    @Test
    @DisplayName("#createClanWithOwner > when owner is not in a clan > when color is invalid > throw error")
    void createClanWithOwnerWhenOwnerIsNotInAClanWhenColorIsInvalidThrowError() {
        Player player1 = create_player();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanManagementService.createClanWithOwner("name", "tag", "#xxxxxxx", player1.getId())
        );
        
        assertAll(
            () -> assertEquals(ErrorMessages.INVALID_CLAN_COLOR, exception.getMessage()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        );
    }

    @Test
    @DisplayName("#createClanWithOwner > when owner is not in a clan > when tag is too long > throw error")
    void createClanWithOwnerWhenOwnerIsNotInAClanWhenTagIsTooLongThrowError() {
        Player player1 = create_player();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanManagementService.createClanWithOwner("name", "tag_to_long", "#000000", player1.getId())
        );
        
        assertAll(
            () -> assertEquals(ErrorMessages.INVALID_CLAN_TAG, exception.getMessage()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        );
    }

    @Test
    @DisplayName("#createClanWithOwner > when owner is not in a clan > when tag is to short > throw error")
    void createClanWithOwnerWhenOwnerIsNotInAClanWhenTagIsToShortThrowError() {
        Player player1 = create_player();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanManagementService.createClanWithOwner("name", "x", "#000000", player1.getId())
        );
        
        assertAll(
            () -> assertEquals(ErrorMessages.INVALID_CLAN_TAG, exception.getMessage()),
            () -> assertFalse(clanFiliationService.checkPlayerInAnyClan(player1.getId()))
        );
    }

    @ParameterizedTest
    @EnumSource(ClanRole.class)
    @DisplayName("#createClanWithOwner > when owner is in a clan > throw error")
    void createClanWithOwnerWhenOwnerIsInAClanThrowError(ClanRole clan_role) {
        Player player1 = create_player();
        Clan original_clan = create_clan("name_", "tag_");
        ClanFiliation original_filiation = create_clan_filiation(original_clan, player1, clan_role);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanManagementService.createClanWithOwner("name", "tag", "#000000", player1.getId())
        );
        
        ClanFiliation final_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(player1.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.PLAYER_IN_A_CLAN, exception.getMessage()),
            () -> assertEquals(final_filiation.getId(), original_filiation.getId())
        );
    }

    @Test
    @DisplayName("#createClanWithOwner > when user is not in a clan > create clan & user is the owner")
    void createClanWithOwnerWhenUserIsNotInAClanCreateClanUserIsTheOwner() {
        Player player1 = create_player();
        
        assertAll(
            () -> assertDoesNotThrow(
                () -> clanManagementService.createClanWithOwner("test", "tag", "#000000", player1.getId())
            ),
            () -> assertEquals(
                ClanRole.OWNER, 
                clanFiliationService.getPlayerFilitiationByPlayerId(player1.getId()).getRole()
            )
        );
    }

    @Test
    @DisplayName("#deleteClanWithOwner > when the member is the owner > delete the clan")
    void deleteClanWithOwnerWhenTheMemberIsTheOwnerDeleteTheClan() {
        Player owner = create_player();
        Player member1 = create_player();
        Player member2 = create_player();
        Player member3 = create_player();
        Clan clan = create_clan("clanname", "tag");


        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member1, ClanRole.MEMBER);
        create_clan_filiation(clan, member2, ClanRole.MEMBER);
        create_clan_filiation(clan, member3, ClanRole.MEMBER);

        assertAll(
            () -> assertDoesNotThrow( () ->
                clanManagementService.deleteClanWithOwner(owner.getId())
            ),
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(owner.getId())
            ),
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(member1.getId())
            ),
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(member2.getId())
            ),
            () -> assertFalse(
                clanFiliationService.checkPlayerInAnyClan(member3.getId())
            )

        );
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#deleteClanWithOwner > when the member is not able to delete > throw error")
    void deleteClanWithOwnerWhenTheMemberIsNotAbleToDeleteThrowError(ClanRole role) {
        Player owner = create_player();
        Player member1 = create_player();
        Player member2 = create_player();
        Player member3 = create_player();
        Clan clan = create_clan("clanname", "tag");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member1, role);
        create_clan_filiation(clan, member2, ClanRole.MEMBER);
        create_clan_filiation(clan, member3, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> clanManagementService.deleteClanWithOwner(member1.getId())
        );

        assertAll(
            () -> assertEquals(
                ErrorMessages.PLAYER_IS_NOT_OWNER_FROM_CLAN,
                exception.getMessage()
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(owner.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member1.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member2.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member3.getId())
            )

        );
    }

    @Test
    @DisplayName("#deleteClanWithOwner > when player is not a member > throw error")
    void deleteClanWithOwnerWhenPlayerIsNotAMemberThrowError() {
        Player owner = create_player();
        Player member1 = create_player();
        Player member2 = create_player();
        Player member3 = create_player();
        Player member4 = create_player();
        Clan clan = create_clan("clanname", "tag");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member1, ClanRole.MEMBER);
        create_clan_filiation(clan, member2, ClanRole.MEMBER);
        create_clan_filiation(clan, member3, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> clanManagementService.deleteClanWithOwner(member4.getId())
        );

        assertAll(
            () -> assertEquals(
                ErrorMessages.PLAYER_NOT_IN_A_CLAN,
                exception.getMessage()
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(owner.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member1.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member2.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member3.getId())
            )

        );
    }

    @Test
    @DisplayName("#deleteClanWithOwner > when member is owner from another clan > other members stays in the clan")
    void deleteClanWithOwnerWhenMemberIsOwnerFromAnotherClanThrowError() {
        Player owner = create_player();
        Player member1 = create_player();
        Player member2 = create_player();
        Player member3 = create_player();
        Player member4 = create_player();
        Clan clan = create_clan("clanname", "tag");
        Clan another_clan = create_clan("clan_name", "tag_");

        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member1, ClanRole.MEMBER);
        create_clan_filiation(clan, member2, ClanRole.MEMBER);
        create_clan_filiation(clan, member3, ClanRole.MEMBER);
        create_clan_filiation(another_clan, member4, ClanRole.OWNER);

        assertAll(
            () -> assertDoesNotThrow( 
                () -> clanManagementService.deleteClanWithOwner(member4.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(owner.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member1.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member2.getId())
            ),
            () -> assertTrue(
                clanFiliationService.checkPlayerInAnyClan(member3.getId())
            )

        );
    }

    @Test
    @DisplayName("#changeClanName > when member is the owner > change the name")
    void changeClanNameWhenMemberIsTheOwnerChangeTheName() {
        Player player = create_player();
        Clan clan = create_clan("firstname", "tag");

        create_clan_filiation(clan, player, ClanRole.OWNER);
        
        assertDoesNotThrow(
            () -> clanManagementService.changeClanName(player.getId(), "secondname")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertEquals("secondname", updated_clan.getName());
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#changeClanName > when member is admin or member > throw error")
    void changeClanNameWhenMemberIsAdminOrMemberThrowError(ClanRole role) {
        Player player = create_player();
        Clan clan = create_clan("firstname", "tag");

        create_clan_filiation(clan, player, role);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanName(player.getId(), "secondname")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals("firstname", updated_clan.getName()),
            () -> assertEquals(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#changeClanTag > when member is the owner > when tag is valid > change the tag")
    void changeClanTagWhenMemberIsTheOwnerWhenTagIsValidChangeTheTag() {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, ClanRole.OWNER);
        
        assertDoesNotThrow(
            () -> clanManagementService.changeClanTag(player.getId(), "xxx")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertEquals("xxx", updated_clan.getTag());
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#changeClanTag > when member is admin or member > when the tag is valid > throw error")
    void changeClanTagWhenMemberIsAdminOrMemberWhenTheTagIsValidThrowError(ClanRole role) {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, role);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanTag(player.getId(), "xxx")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals("yyy", updated_clan.getTag()),
            () -> assertEquals(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#changeClanTag > when member is the owner > when tag is invalid > throw error")
    void changeClanTagWhenMemberIsTheOwnerWhenTagIsInvalidThrowError() {
        Player player = create_player();
        Clan clan = create_clan("firstname", "tag");

        create_clan_filiation(clan, player, ClanRole.OWNER);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanTag(player.getId(), "invalid clan tag")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.INVALID_CLAN_TAG, exception.getMessage()),
            () -> assertEquals("tag", updated_clan.getTag())
        );
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#changeClanTag > when member is admin or member > when tag is invalid > throw error")
    void changeClanTagWhenMemberIsAdminOrMemberWhenTagIsInvalidThrowError(ClanRole role) {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, role);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanTag(player.getId(), "xxxxx")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals("yyy", updated_clan.getTag()),
            () -> assertEquals(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#changeClanColor > when member is the owner > when color is valid > change color")
    void changeClanColorWhenMemberIsTheOwnerWhenColorIsValidChangeColor() {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, ClanRole.OWNER);
        
        assertDoesNotThrow(
            () -> clanManagementService.changeClanColor(player.getId(), "#ffffff")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertEquals("#ffffff", updated_clan.getColor());
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#changeClanColor > when member is admin or member > when color is valid > throw error")
    void changeClanColorWhenMemberIsAdminOrMemberWhenColorIsValidThrowError(ClanRole role) {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, role);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanColor(player.getId(), "#ffffff")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals("#000000", updated_clan.getColor()),
            () -> assertEquals(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#changeClanColor > when member is the owner > when color is invalid > throw error")
    void changeClanColorWhenMemberIsTheOwnerWhenColorIsInvalidThrowError() {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, ClanRole.OWNER);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanColor(player.getId(), "#00000p")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.INVALID_CLAN_COLOR, exception.getMessage()),
            () -> assertEquals("#000000", updated_clan.getColor())
        );
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#changeClanColor > when member is admin or member > when color is invalid > throw error")
    void changeClanColorWhenMemberIsAdminOrMemberWhenColorIsInvalidThrowError(ClanRole role) {
        Player player = create_player();
        Clan clan = create_clan("firstname", "yyy");

        create_clan_filiation(clan, player, role);
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> clanManagementService.changeClanColor(player.getId(), "#00000p")
        );
        
        Clan updated_clan = clanService.getClanById(clan.getId());

        assertAll(
            () -> assertEquals("#000000", updated_clan.getColor()),
            () -> assertEquals(ErrorMessages.ONLY_OWNER_CAN_CHANGE_CLAN_INFORMATION, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#promoteMember > when initiator is owner > when the target is member > promote member to admin")
    void promoteMemberWhenInitiatorIsOwnerWhenTheTargetIsMemberPromoteMemberToAdmin() {
        Player owner = create_player();
        Player member = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member, ClanRole.MEMBER);

        assertAll(
            () -> assertDoesNotThrow(
                () -> clanFiliationService.promotePlayer(owner.getId(), member.getId())
            ),
            () -> assertEquals(
                ClanRole.ADMIN, 
                clanFiliationService.getPlayerFilitiationByPlayerId(member.getId()).getRole()
            )
        );
    }

    @Test
    @DisplayName("#promoteMember > when initiator is owner > when the target is an admin > throw error ")
    void promoteMemberWhenInitiatorIsOwnerWhenTheTargetIsAnAdminThrowError() {
        Player owner = create_player();
        Player member = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clanFiliationService.promotePlayer(owner.getId(), member.getId())
            );

        assertAll(
            () -> assertEquals(
                ErrorMessages.PLAYER_CANNOT_BE_PROMOTED, exception.getMessage()
            ),
            () -> assertEquals(
                ClanRole.ADMIN, 
                clanFiliationService.getPlayerFilitiationByPlayerId(member.getId()).getRole()
            )
        );
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#promoteMember > when initiator is a member or admin > when target is member > throw error")
    void promoteMemberWhenInitiatorIsAMemberOrAdminWhenTargetIsMemberThrowError(ClanRole role) {
        Player member1 = create_player();
        Player member2 = create_player();
        Clan clan = create_clan("name", "tag");
        
        create_clan_filiation(clan, member1, role);
        create_clan_filiation(clan, member2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanFiliationService.promotePlayer(member1.getId(), member2.getId()) 
        );

        ClanFiliation final_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(member2.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.MEMBER_DOES_NOT_HAVE_PROMOTE_PERMISSION, exception.getMessage()),
            () -> assertEquals(ClanRole.MEMBER, final_filiation.getRole())
        );
    }

    @Test
    @DisplayName("#promoteMember > when initiator is owner > when target is in another clan > throw exception")
    void promoteMemberWhenInitiatorIsOwnerWhenTargetIsInAnotherClanThrowException() {
        Player member1 = create_player();
        Player member2 = create_player();
        Clan clan = create_clan("name", "tag");
        Clan secondary_clan = create_clan("name", "tag");
        
        create_clan_filiation(clan, member1, ClanRole.OWNER);
        create_clan_filiation(secondary_clan, member2, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanFiliationService.promotePlayer(member1.getId(), member2.getId()) 
        );

        ClanFiliation final_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(member2.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.PLAYERS_ARE_NOT_IN_SAME_CLAN, exception.getMessage()),
            () -> assertEquals(ClanRole.MEMBER, final_filiation.getRole())
        );
    }

    @Test
    @DisplayName("#promoteMember > when member is not in any clan > when target is not in any clan > throw error")
    void promoteMemberWhenMemberIsNotInAnyClanWhenTargetIsNotInAnyClanThrowError() {
        Player member1 = create_player();
        Player member2 = create_player();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanFiliationService.promotePlayer(member1.getId(), member2.getId()) 
        );

        assertAll(
            () -> assertEquals(ErrorMessages.PLAYER_NOT_IN_A_CLAN, exception.getMessage())
        );
    }

    @Test
    @DisplayName("#demotePlayer > when initiator is owner > when target is admin > demote player")
    void demotePlayerWhenInitiatorIsOwnerWhenTargetIsAdminDemotePlayer() {
        
        Player owner = create_player();
        Player member = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member, ClanRole.ADMIN);

        assertAll(
            () -> assertDoesNotThrow(
                () -> clanFiliationService.demotePlayer(owner.getId(), member.getId())
            ),
            () -> assertEquals(
                ClanRole.MEMBER, 
                clanFiliationService.getPlayerFilitiationByPlayerId(member.getId()).getRole()
            )
        );
    }

    @Test
    @DisplayName("#demotePlayer > when initiator is owner > when target is member > throw error")
    void demotePlayerWhenInitiatorIsOwnerWhenTargetIsMemberThrowError() {
        Player owner = create_player();
        Player member = create_player();
        Clan clan = create_clan("name", "tag");
        create_clan_filiation(clan, owner, ClanRole.OWNER);
        create_clan_filiation(clan, member, ClanRole.MEMBER);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clanFiliationService.demotePlayer(owner.getId(), member.getId())
            );

        assertAll(
            () -> assertEquals(
                ErrorMessages.PLAYER_CANNOT_BE_DEMOTED, exception.getMessage()
            ),
            () -> assertEquals(
                ClanRole.MEMBER, 
                clanFiliationService.getPlayerFilitiationByPlayerId(member.getId()).getRole()
            )
        );
    }

    @ParameterizedTest
    @EnumSource(value = ClanRole.class, names = {"ADMIN", "MEMBER"})
    @DisplayName("#demotePlayer > when initiator is admin or member > when target is admin > throw error")
    void demotePlayerWhenInitiatorIsAdminOrMemberWhenTargetIsAdminThrowError(ClanRole role) {
        Player member1 = create_player();
        Player member2 = create_player();
        Clan clan = create_clan("name", "tag");
        
        create_clan_filiation(clan, member1, role);
        create_clan_filiation(clan, member2, ClanRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> clanFiliationService.demotePlayer(member1.getId(), member2.getId()) 
        );

        ClanFiliation final_filiation = clanFiliationService.getPlayerFilitiationByPlayerId(member2.getId());

        assertAll(
            () -> assertEquals(ErrorMessages.MEMBER_DOES_NOT_HAVE_DEMOTE_PERMISSION, exception.getMessage()),
            () -> assertEquals(ClanRole.ADMIN, final_filiation.getRole())
        );
    }

    @Test
    @DisplayName("#getAllClans > when there is valid clans > return all clans")
    void getAllClansWhenThereIsValidClansReturnAllClans() {
        create_clan("clan1", "tag1");
        Clan clan2 = create_clan("clan2", "tag2");
        create_clan("clan3", "tag3");
        create_clan("clan4", "tag4");
        create_clan("clan5", "tag5");

        clan2.setDeleted_at(LocalDateTime.now());
        clanRepository.save(clan2);

        assertAll(
            () -> assertEquals(4, clanService.getAllClans().size()),
            () -> assertEquals("clan1", clanService.getAllClans().get(0).getName()),
            () -> assertEquals("clan3", clanService.getAllClans().get(1).getName()),
            () -> assertEquals("clan4", clanService.getAllClans().get(2).getName()),
            () -> assertEquals("clan5", clanService.getAllClans().get(3).getName())
        );

    }

}


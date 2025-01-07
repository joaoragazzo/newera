package com.new_era.alpha.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.controllers.dto.ClanInfoDTO;
import com.new_era.alpha.controllers.dto.ClanMemberDTO;
import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.security.UserSession;
import com.new_era.alpha.services.clan.ClanFiliationService;
import com.new_era.alpha.services.clan.ClanInvitationService;
import com.new_era.alpha.services.clan.ClanManagementService;
import com.new_era.alpha.services.clan.ClanService;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clan")
@AllArgsConstructor
public class ClanController {
    
    ClanService clanService;
    ClanManagementService clanManagementService;
    ClanFiliationService clanFiliationService;
    ClanInvitationService clanInvitationService;
    PlayerService playerService;
    UserSession session;

    @PostMapping("/all")
    public List<Clan> getAllClans() {
        return clanService.getAllClans();
    }

    @PostMapping("/unaffiliated")
    public List<Player> getUnaffiliatedPlayers() {
        return clanFiliationService.unaffiliatedPlayers();
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody Map<String, String> payload) {

        Integer player_id = session.getPlayer_id();
    
        Map<String, String> response = new HashMap<>();

        String clan_name = payload.get("name");
        String clan_tag = payload.get("tag");
        String clan_color = payload.get("color");

        clanManagementService.createClanWithOwner(clan_name, clan_tag, clan_color, player_id);

        response.put("success", "Clan criado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> delete() {
        
        Integer player_id = session.getPlayer_id();
        Map<String, String> response = new HashMap<>();

        clanManagementService.deleteClanWithOwner(player_id);


        response.put("message", "Clan desativado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, String>> edit(@RequestBody Map<String, String> payload) {
        Integer player_id = session.getPlayer_id();
        String color = payload.get("color");
        String tag = payload.get("tag");
        String name = payload.get("name");
        clanManagementService.changeClanColor(player_id, color);
        clanManagementService.changeClanTag(player_id, tag);
        clanManagementService.changeClanName(player_id, name);

        Map<String, String> response = new HashMap<>();

        response.put("success", "As informações foram alteradas com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    
    }

    @PostMapping("/info")
    public ResponseEntity<ClanInfoDTO> info() {
        
        Integer player_id = session.getPlayer_id();
        ClanFiliation filiation = clanFiliationService.getPlayerFilitiationByPlayerId(player_id);
        Clan clan = filiation.getClan();
        List<ClanFiliation> filiations = clanFiliationService.getAllActiveClanFiliationsByClanId(clan.getId());
        List<ClanMemberDTO> members = new ArrayList<>();
        
        for(ClanFiliation generic_filiation : filiations) {
            Player player = generic_filiation.getPlayer();
            String nickname = playerService.getLastNick(player.getId());

            ClanMemberDTO member = new ClanMemberDTO(player.getId(), nickname, 0, 0, LocalDateTime.now(), generic_filiation.getRole() );
            members.add(member);
        }

        ClanInfoDTO response = new ClanInfoDTO(clan.getName(), clan.getTag(), clan.getColor(), filiation.getRole(), members);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/invite")
    public ResponseEntity<Map<String, String>> invite(@RequestBody Map<String, String> payload) {
        Integer inviteer_id = session.getPlayer_id();
        Integer invitee_id = Integer.valueOf(payload.get("invitee_id"));
        clanInvitationService.invitePlayerToClan(inviteer_id, invitee_id);
        
        Map<String, String> response = new HashMap<>();
        response.put("success", "O jogador foi convidado com sucesso!");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<Map<String, String>> acceptInvite(@PathVariable("id") Integer id) {
        Integer player_id = session.getPlayer_id();
        clanInvitationService.acceptInviteToClan(player_id, id);
        
        Map<String, String> response = new HashMap<>();
        response.put("success", "Você aceitou o convite com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/decline/{id}")
    public ResponseEntity<Map<String, String>> declineInvite(@PathVariable("id") Integer id) {
        Integer player_id = session.getPlayer_id();
        clanInvitationService.declineInviteToClan(player_id, id);
        
        Map<String, String> response = new HashMap<>();
        response.put("success", "Você recusou o convite com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/kick/{player_id}")
    public ResponseEntity<Map<String, String>> kickPlayer(@PathVariable("player_id") Integer target_id) {
        Integer initiator_id = session.getPlayer_id();
        clanFiliationService.kickPlayerFromClan(initiator_id, target_id);

        Map<String, String> response = new HashMap<>();
        response.put("success", "Expulsão concluída com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/promote/{player_id}")
    public ResponseEntity<Map<String, String>> promotePlayer(@PathVariable("player_id") Integer target_id) {
        Integer initiator_id = session.getPlayer_id();
        clanFiliationService.promotePlayer(initiator_id, target_id);

        Map<String, String> response = new HashMap<>();
        response.put("success", "Promoção concluída com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/demote/{player_id}")
    public ResponseEntity<Map<String, String>> demotePlayer(@PathVariable("player_id") Integer target_id) {
        Integer initiator_id = session.getPlayer_id();
        clanFiliationService.demotePlayer(initiator_id, target_id);

        Map<String, String> response = new HashMap<>();
        response.put("success", "Rebaixamento concluído com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

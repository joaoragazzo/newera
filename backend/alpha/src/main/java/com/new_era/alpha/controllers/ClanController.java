package com.new_era.alpha.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.controllers.dto.ClanInfoDTO;
import com.new_era.alpha.controllers.dto.ClanMemberDTO;
import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.entities.clan.ClanFiliation;
import com.new_era.alpha.security.UserSession;
import com.new_era.alpha.services.clan.ClanFiliationService;
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
    PlayerService playerService;
    UserSession session;

    @PostMapping("/all")
    public List<Clan> getAllClans() {
        return clanService.getAllClans();
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
        Clan clan = clanFiliationService.getPlayerClanByPlayerId(player_id);
        String color = payload.get("color");
        String tag = payload.get("tag");
        String name = payload.get("name");
        clanService.changeColor(clan.getId(), color);
        clanService.changeTag(clan.getId(), tag);
        clanService.changeName(clan.getId(), name);

        Map<String, String> response = new HashMap<>();

        response.put("success", "As informações foram alteradas com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    
    }

    @PostMapping("/info")
    public ResponseEntity<ClanInfoDTO> info() {
        
        Integer player_id = session.getPlayer_id();
        Clan clan = clanFiliationService.getPlayerClanByPlayerId(player_id);
        List<ClanFiliation> filiations = clanFiliationService.getAllActiveClanFiliationsByClanId(clan.getId());
        List<ClanMemberDTO> members = new ArrayList<>();
        
        for(ClanFiliation filiation : filiations) {
            String nickname = playerService.getLastNick(player_id);

            ClanMemberDTO member = new ClanMemberDTO(nickname, 0, 0, LocalDateTime.now(), filiation.getRole() );
            members.add(member);
        }

        ClanInfoDTO response = new ClanInfoDTO(clan.getName(), clan.getTag(), clan.getColor(), members);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

}

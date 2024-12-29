package com.new_era.alpha.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.security.UserSession;
import com.new_era.alpha.services.clan.ClanManagementService;
import com.new_era.alpha.services.clan.ClanService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clan")
@AllArgsConstructor
public class ClanController {
    
    ClanService clanService;
    ClanManagementService clanManagementService;
    UserSession session;

    @PostMapping("/all")
    public List<Clan> getAllClans() {
        return clanService.getAllClans();
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createClan(@RequestBody Map<String, String> payload) {

        Integer player_id = session.getPlayer_id();
    
        Map<String, String> response = new HashMap<>();

        String clan_name = payload.get("name");
        String clan_tag = payload.get("tag");
        String clan_color = payload.get("color");

        try {
            clanManagementService.createClanWithOwner(clan_name, clan_tag, clan_color, player_id);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        }

        response.put("success", "Clan criado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteClan(HttpSession session) {
        
        Integer player_id = (Integer) session.getAttribute("player_id");
        Map<String, String> response = new HashMap<>();

        try {
            clanManagementService.deleteClanWithOwner(player_id);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        }

        response.put("message", "Clan desativado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PostMapping("/members")
    // public ResponseEntity<Map<String, String>> fetchMembers(HttpSession session) {
    //     Integer player_id = (Integer) session.getAttribute("player_id");
    // }
    
}

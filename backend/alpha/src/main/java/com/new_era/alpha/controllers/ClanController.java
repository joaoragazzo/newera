package com.new_era.alpha.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.services.clan.ClanManagementService;
import com.new_era.alpha.services.clan.ClanService;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/clan")
public class ClanController {
    
    ClanService clanService;
    ClanManagementService clanManagementService;

    @PostMapping("/all")
    public List<Clan> getAllClans() {
        return clanService.getAllClans();
    }

    @PostMapping("/createClan")
    public ResponseEntity<Map<String, String>> createClan(HttpSession session, @RequestBody Clan clan) {

        Integer player_id = (Integer) session.getAttribute("player_id");
        
        Map<String, String> response = new HashMap<>();

        try {
            clanManagementService.createClanWithOwner(clan.getName(), clan.getTag(), clan.getColor(), player_id);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        }

        response.put("success", "Clan criado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteClan")
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

    
}

package com.new_era.alpha.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/player")
@AllArgsConstructor
public class PlayerController {
    
    PlayerService playerService;

    @GetMapping("/list")
    public ResponseEntity<Map<Integer, String>> getAllPlayers() {
        List<Player> players = playerService.findAll();

        Map<Integer, String> response = new HashMap<>();

        for (Player player : players) {
            String nick = playerService.getLastNick(player.getId());
            response.put(player.getId(), nick);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

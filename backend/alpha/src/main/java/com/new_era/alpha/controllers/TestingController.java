package com.new_era.alpha.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.player.Nick;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.security.UserSession;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/debug")
public class TestingController {

    private final PlayerService playerService;
    private final UserSession session;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public List<Nick> getAllNicks(@PathVariable("id") Integer id) {
        return playerService.getAllPlayerNicks(id);
    }

    @GetMapping("/player/insertnick/{player_id}/{nickname}")
    public Boolean setNewNickname(@PathVariable("player_id") Integer player_id,
            @PathVariable("nickname") String nickname) {
        return playerService.createNewNick(player_id, nickname);
    }

    @PostMapping("/session")
    public ResponseEntity<Map<String, String>> getCurrentSession() {
        String player_name = playerService.getLastNick(session.getPlayer_id());
        
        Map<String, String> response = new HashMap<>();
        response.put("player", player_name);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/setsteam64id")
    public ResponseEntity<String> setSteam64id(@RequestBody Map<String, String> payload) {
        Integer player_id = Integer.valueOf(payload.get("player"));
        session.setPlayer_id(player_id);
        return ResponseEntity.ok("OK session setted to " + session.getPlayer_id());
    } 

}

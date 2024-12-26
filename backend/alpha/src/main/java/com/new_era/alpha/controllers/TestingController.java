package com.new_era.alpha.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.player.Nick;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TestingController {
    
    private final PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public List<Nick> getAllNicks(@PathVariable("id") Integer id) {
        return playerService.getAllPlayerNicks(id);
    }

    @GetMapping("/player/insertnick/{player_id}/{nickname}")
    public Boolean setNewNickname(@PathVariable("player_id") Integer player_id, @PathVariable("nickname") String nickname) {
        return playerService.createNewNick(player_id, nickname);
    }
}

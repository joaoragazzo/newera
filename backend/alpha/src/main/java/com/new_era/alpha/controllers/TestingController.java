package com.new_era.alpha.controllers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_era.alpha.entities.player.Nick;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.player.NickRepository;
import com.new_era.alpha.repositories.player.PlayerRepository;
import com.new_era.alpha.services.player.PlayerService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/debug")
public class TestingController {

    private final PlayerService playerService;

    private final PlayerRepository playerRepository;
    private final NickRepository nickRepository;

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

    @GetMapping("/defaultdb")
    public ResponseEntity<Map<String, String>> defaultdb() {
        Map<String, String> response = new HashMap<>();

        Player player;

        for (int i = 0; i < 100; i++) {
            player = new Player();
            player.setSteam64id(BigInteger.valueOf(76561198118616961L + i));
            player = playerRepository.save(player);

            Nick nick = new Nick();
            nick.setCreated_at(LocalDateTime.now().minusDays(3));
            nick.setName(String.format("player_%d_old", i));
            nick.setPlayer(player);
            nickRepository.save(nick);

            nick = new Nick();
            nick.setCreated_at(LocalDateTime.now().minusDays(1));
            nick.setName(String.format("player_%d_new", i));
            nick.setPlayer(player);
            nickRepository.save(nick);
        }

        response.put("message", "default values was successfully created");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}

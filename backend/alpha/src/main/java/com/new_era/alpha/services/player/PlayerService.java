package com.new_era.alpha.services.player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.player.Nick;
import com.new_era.alpha.entities.player.Player;
import com.new_era.alpha.repositories.player.NickRepository;
import com.new_era.alpha.repositories.player.PlayerRepository;
import com.new_era.alpha.services.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerService {
    
    private final PlayerRepository playerRepository;
    private final NickRepository nickRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Nick> getAllPlayerNicks(Integer player_id) {
        Optional<Player> player = playerRepository.findById(player_id);
    
        if (player.isPresent()) {
            return player.get().getNicks();
        }
        
        return null;
    }

    public Boolean createNewNick(Integer player_id, String nick) {
        Nick nickname = new Nick();
        Player player = new Player();

        player.setId(player_id);
        nickname.setPlayer(player);
        nickname.setName(nick);
        nickname.setCreated_at(LocalDateTime.now());
        
        nickRepository.save(nickname);
        
        return Boolean.TRUE;
    }

    public String getLastNick(Integer player_id) {
        Nick nick = nickRepository.findMostRecentNickByPlayerId(player_id).
            orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PLAYER_DOES_NOT_HAVE_A_NICK));
        
        return nick.getName();
    }    

    public Player getPlayerById(Integer player_id) {
        Player player = playerRepository.findById(player_id).orElseThrow(
            () -> new IllegalArgumentException(String.format(ErrorMessages.PLAYER_NOT_FOUND, player_id))
        );

        return player;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

}

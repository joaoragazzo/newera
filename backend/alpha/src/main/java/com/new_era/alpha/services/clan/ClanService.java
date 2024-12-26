package com.new_era.alpha.services.clan;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.new_era.alpha.entities.clan.Clan;
import com.new_era.alpha.repositories.clan.ClanRepository;
import com.new_era.alpha.services.messages.ErrorMessages;
import com.new_era.alpha.services.utils.color.HexColorValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClanService {
    
    private final ClanRepository clanRepository;

    public Clan getClanById(Integer clan_id) {
        return clanRepository.findById(clan_id).orElseThrow(
            () -> new IllegalArgumentException(String.format(ErrorMessages.CLAN_DOES_NOT_EXISTS, clan_id))
        );
    }
    
    public Clan createClan(String tag, String name, String color) {
        if (!HexColorValidator.isValidHexColor(color)) 
            throw new IllegalArgumentException(ErrorMessages.INVALID_CLAN_COLOR);
        
        if (tag.length() < 2 || tag.length() > 4)
            throw new IllegalArgumentException(ErrorMessages.INVALID_CLAN_TAG);

        Clan clan = new Clan();
        clan.setTag(tag);
        clan.setName(name);
        clan.setColor(color);
        clan.setCreated_at(LocalDateTime.now());
        
        return clanRepository.save(clan);
    }

    public Clan changeTag(Integer clan_id, String tag) {
        if (tag.length() < 2 || tag.length() > 4)
            throw new IllegalArgumentException(ErrorMessages.INVALID_CLAN_TAG);

        Clan clan = getClanById(clan_id);
        clan.setTag(tag);
        return clanRepository.save(clan);
    }

    public Clan changeName(Integer clan_id, String name) {
        Clan clan = getClanById(clan_id);
        clan.setName(name);
        return clanRepository.save(clan);
    }

    public Clan changeColor(Integer clan_id, String color) {
        if (!HexColorValidator.isValidHexColor(color)) 
            throw new IllegalArgumentException(ErrorMessages.INVALID_CLAN_COLOR);
        
        Clan clan = getClanById(clan_id);
        clan.setColor(color);
        return clanRepository.save(clan);
    }
    
    public List<Clan> getAllClans() {
        return clanRepository.findAllActiveClans();
    }
}

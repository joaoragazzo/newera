package com.new_era.alpha.entities.clan;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.new_era.alpha.entities.enums.ClanRole;
import com.new_era.alpha.entities.player.Player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "clan_filiation")
public class ClanFiliation {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @JsonIgnore
    private Player player;

    @ManyToOne
    @JoinColumn(name = "clan_id", nullable = false)
    @JsonIgnore
    private Clan clan;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ClanRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joined_at;

    @Column(name = "left_at", nullable = true)
    private LocalDateTime left_at;
    

}

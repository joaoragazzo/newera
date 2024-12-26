package com.new_era.alpha.entities.clan;

import java.time.LocalDateTime;

import com.new_era.alpha.entities.player.Player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clan_invitation")
public class ClanInvitation {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "clan_id", nullable = false)
    private Clan clan;

    @Column(name  = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expire_at;

    @Column(name = "accepted_at", nullable = true)
    private LocalDateTime accepted_at;

    @Column(name = "declined_at", nullable = true)
    private LocalDateTime declined_at;

    @Column(name = "accepted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean accepted = false;

    @Column(name = "declined", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean declined = false;

}

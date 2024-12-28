package com.new_era.alpha.entities.player;

import java.math.BigInteger;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "player")
public class Player {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "steam64id", nullable = false, unique = true)
    private BigInteger steam64id;


    @Column(name = "discord_id", nullable = true, unique = true)
    private BigInteger discord_id;

    @Column(name = "karma")
    private Integer karma = 500;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Nick> nicks;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Notification> notifications;
}

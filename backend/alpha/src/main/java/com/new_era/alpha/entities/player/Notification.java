package com.new_era.alpha.entities.player;

import java.time.LocalDateTime;


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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notification")
public class Notification {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "seen_at", nullable = true)
    private LocalDateTime seen_at;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;
    
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player; 

    @Column(name = "href", nullable = true)
    private String href;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "callback_for_accept", nullable = true)
    private String callback_for_accept;

    @Column(name = "callback_for_decline", nullable = true)
    private String callback_for_decline;
}

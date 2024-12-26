package com.new_era.alpha.entities.clan;

import java.time.LocalDateTime;
import java.util.List;

import com.new_era.alpha.services.utils.color.ValidHexColor;

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
@Table(name = "clan")
public class Clan {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tag", nullable = false)
    private String tag;

    @Column(name = "color", nullable = false)
    @ValidHexColor
    private String color;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deleted_at;

    @OneToMany(mappedBy = "clan")
    private List<ClanFiliation> filiations;

    public boolean isClosed() {
        return this.deleted_at != null;
    }

}

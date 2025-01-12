package com.new_era.alpha.entities.shop;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tag")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag", nullable = false)
    private String tag;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deleted_at;

    @ManyToMany(mappedBy = "tags")
    private List<Item> itens;

}


package com.new_era.alpha.entities.shop;

import java.time.LocalDateTime;
import java.util.List;

import com.new_era.alpha.entities.enums.ItemTypes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name="item")
public class Item {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /* Informações básicas */

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "stock", nullable = true)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ItemTypes type;

    /* Mídia */

    @Column(name = "thumbnail", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String thumbnail;

    @ElementCollection
    @Column(name = "images", nullable = true, columnDefinition = "MEDIUMTEXT")
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
    private List<String> images;

    @ElementCollection
    @Column(name = "youtube_links", nullable = true)
    private List<String> youtube_links;

    
    /* Configurações adicionais */
    
    @Column(name = "manual_delivery", nullable = false)
    private Boolean manual_delivery = false;

    @Column(name = "notify_private_discord", nullable = false)
    private Boolean notify_private_discord = false;

    @Column(name = "notify_public_discord", nullable = false)
    private Boolean notify_public_discord = false;

    @Column(name = "notify_site", nullable = false)
    private Boolean notify_site = false;

    @Column(name = "show_acquisitions", nullable = false)
    private Boolean show_acquisitions = false;

    @Column(name = "allow_rating", nullable = false)
    private Boolean allow_rating = false;

    @Column(name = "allow_comments", nullable = false)
    private Boolean allow_comments = false;

    /*  Datas */

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deleted_at;

    /* Mapeamento */

    @ManyToMany
    @JoinTable(
        name = "item_tag",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = true)
    private Subcategory subcategory;

}
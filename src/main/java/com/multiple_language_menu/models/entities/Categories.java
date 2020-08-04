package com.multiple_language_menu.models.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories extends BaseEntity{
    private String name;
    private String description;
    private Integer rank;

    @OneToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Categories categoriesParent;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Shops shop;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Items> items;

    @OneToMany(mappedBy = "category" )
    private Collection<CategoriesTranslates> categoriesTranslates;
}

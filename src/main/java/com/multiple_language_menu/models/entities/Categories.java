package com.multiple_language_menu.models.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Categories categoriesParent;


    @ManyToOne
    @JoinColumn(name = "shop_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Shops shop;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Items> items = new ArrayList<Items>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Collection<CategoriesTranslates> categoriesTranslates;

    @OneToMany(mappedBy = "categoriesParent", cascade = CascadeType.REMOVE)
    private Collection<Categories> childCategory = new ArrayList<Categories>();
}

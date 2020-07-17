package com.multiple_language_menu.models.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    private String name;
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Categories categoriesParent;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Items> items;

    @OneToMany(mappedBy = "category")
    private Collection<CategoriesTranslates> categoriesTranslates;
}

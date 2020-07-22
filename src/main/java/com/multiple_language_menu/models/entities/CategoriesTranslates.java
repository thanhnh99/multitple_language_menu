package com.multiple_language_menu.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesTranslates extends BaseEntity{

    private String languageCode;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
}

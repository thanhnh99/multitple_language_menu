package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.models.request.ReqTranslateCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public CategoriesTranslates(ReqTranslateCategory requestData)
    {
        this.languageCode = requestData.getLanguageCode();
        this.name = requestData.getName();
        this.description = requestData.getDescription();
    }
}

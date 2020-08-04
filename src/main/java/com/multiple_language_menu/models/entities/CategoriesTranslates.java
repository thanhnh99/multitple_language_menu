package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.models.request.ReqTransCategory;
import com.multiple_language_menu.models.request.ReqTranslateItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public CategoriesTranslates(ReqTransCategory requestData)
    {
        this.languageCode = requestData.getLanguageCode();
        this.name = requestData.getName();
        this.description = requestData.getDescription();
    }
}

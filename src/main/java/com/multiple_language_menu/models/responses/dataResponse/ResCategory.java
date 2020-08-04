package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;

import java.util.List;

public class ResCategory {
    private Integer rank;
    private String categoryId;
    private String name;
    private String description;


    public ResCategory(CategoriesTranslates categoriesTranslates)
    {
        this.rank = categoriesTranslates.getCategory().getRank();
        this.categoryId = categoriesTranslates.getCategory().getId();
        this.name = categoriesTranslates.getName();
        this.description = categoriesTranslates.getDescription();
    }

    public ResCategory(Categories category)
    {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.rank = category.getRank();
    }
}



package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.CategoriesTranslates;

import java.util.List;

public class ResCategory {
    private String categoryId;
    private String name;
    private String description;


    public ResCategory(CategoriesTranslates categoriesTranslates)
    {
        this.categoryId = categoriesTranslates.getCategory().getId();
        this.name = categoriesTranslates.getName();
        this.description = categoriesTranslates.getDescription();
    }
}



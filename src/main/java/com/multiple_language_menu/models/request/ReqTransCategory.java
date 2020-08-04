package com.multiple_language_menu.models.request;

import com.multiple_language_menu.models.entities.Categories;
import lombok.Data;

@Data
public class ReqTransCategory {
    private String languageCode;
    private String categoryId;
    private String name;
    private String description;

    public ReqTransCategory(Categories requestData)
    {
        this.categoryId = requestData.getId();
        this.name = requestData.getName();
        this.description = requestData.getDescription();
    }
}

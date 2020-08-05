package com.multiple_language_menu.models.request;

import com.multiple_language_menu.enums.EActionType;
import com.multiple_language_menu.enums.ETargetType;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;
import com.multiple_language_menu.models.entities.Items;
import lombok.Data;

@Data
public class ReqCreateLog {
    private String target;
    private ETargetType targetType ;
    private String action;
    private EActionType actionType;
    private String shopId;


    public ReqCreateLog(CategoriesTranslates categoriesTranslates)
    {
        this.target = categoriesTranslates.getCategory().getId();
        this.targetType = ETargetType.CATEGORY;
        this.action = "dich";
        this.actionType = EActionType.TRANSLATE;
        this.shopId = categoriesTranslates.getCategory().getShop().getId();
    }

    public ReqCreateLog(Categories category)
    {
        this.target = category.getId();
        this.targetType = ETargetType.CATEGORY;
        this.action = "TRANSLATE";
        this.actionType = EActionType.TRANSLATE;
        this.shopId = category.getShop().getId();
    }

    public ReqCreateLog(Items items)
    {
        this.target = items.getId();
        this.targetType = ETargetType.CATEGORY;
        this.action = "TRANSLATE";
        this.actionType = EActionType.TRANSLATE;
        this.shopId = items.getCategory().getShop().getId();
    }
}

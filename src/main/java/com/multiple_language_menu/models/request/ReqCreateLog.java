package com.multiple_language_menu.models.request;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.ETargetType;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.Shops;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqCreateLog {
    private String targetId;
    private String targetName;
    private ETargetType targetType ;
    private EActionType actionType;
    private String shopId;


    public ReqCreateLog(Categories category, EActionType actionType)
    {
        this.targetId  = category.getId();
        this.targetName = category.getName();
        this.targetType = ETargetType.CATEGORY;
        this.actionType = actionType;
        this.shopId = category.getShop().getId();
    }


    public ReqCreateLog(Items items, EActionType actionType)
    {
        this.targetId = items.getId();
        this.targetName = items.getName() ;
        this.targetType = ETargetType.CATEGORY;
        this.actionType = actionType;
        this.shopId = items.getCategory().getShop().getId();
    }

    public ReqCreateLog(Shops shops, EActionType actionType)
    {
        this.targetId = shops.getId();
        this.targetName = shops.getName();
        this.targetType = ETargetType.SHOP;
        this.actionType = actionType;
        this.shopId = shops.getId();
    }

}

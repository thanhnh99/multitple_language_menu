package com.multiple_language_menu.models.request;

import com.multiple_language_menu.models.entities.Items;
import lombok.Data;

@Data
public class ReqTranslateItem {
    private String languageCode;
    private String itemId;
    private String itemName;
    private String description;

    public ReqTranslateItem(Items item)
    {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.description = item.getDescription();
    }

}

package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.ItemsTranslates;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResItem {
    private String itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer rank;

    public ResItem(ItemsTranslates itemsTranslates)
    {
        this.name = itemsTranslates.getName();
        this.itemId = itemsTranslates.getItem().getId();
        this.description = itemsTranslates.getDescription();
        this.price = itemsTranslates.getItem().getPrice();
        this.rank = itemsTranslates.getItem().getRank();
    }

    public ResItem(Items item)
    {
        this.name = item.getName();
        this.itemId = item.getId();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.rank = item.getRank();
    }
}

package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.ItemsTranslates;

import java.math.BigDecimal;

public class ResItem {
    private String itemId;
    private String name;
    private String description;
    private BigDecimal price;

    public ResItem(ItemsTranslates itemsTranslates)
    {
        this.name = itemsTranslates.getName();
        this.itemId = itemsTranslates.getItem().getId();
        this.description = itemsTranslates.getDescription();
        this.price = itemsTranslates.getItem().getPrice();
    }
}

package com.multiple_language_menu.models.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReqCreateItem {
    private String categoryId;
    private String itemName;
    private BigDecimal price;
    private String description;

}

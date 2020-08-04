package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateCategory {
    private String ShopId;
    private String parentCategory;
    private String categoryName;
    private String description;

}

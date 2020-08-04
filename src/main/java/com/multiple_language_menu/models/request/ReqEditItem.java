package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEditItem {
    private String categoryId;
    private Object itemIds;
    private String itemName;
    private String price;
    private String description;
}

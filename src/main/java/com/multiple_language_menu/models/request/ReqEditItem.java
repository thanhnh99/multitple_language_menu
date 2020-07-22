package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEditItem {
    private String categoryId;
    private String itemName;
    private String price;
    private String description;
    private Integer rank;
}

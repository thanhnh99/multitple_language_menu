package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqEditCategory {
    private Object categoryIds;
    private String parentId;
    private String categoryName;
    private String description;

}

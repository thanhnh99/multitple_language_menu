package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqEditCategory {
    private Integer rank;
    private String categoryId;
    private String CategoryId;
    private String categoryName;
    private String description;

}

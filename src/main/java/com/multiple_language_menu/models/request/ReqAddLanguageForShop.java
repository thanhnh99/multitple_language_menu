package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqAddLanguageForShop {
    private String shopId;
    private String languageCode;
}

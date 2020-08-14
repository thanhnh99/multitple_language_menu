package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUpdateLanguageRank {
    String shopId;
    List<String> languageIds;
}

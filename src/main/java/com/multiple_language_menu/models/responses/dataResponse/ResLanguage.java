package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Languages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResLanguage {
    private String languageId;
    private String code;
    private String name;
    private Integer rank;

    public ResLanguage(Languages languageEntity)
    {
        this.languageId = languageEntity.getId();
        this.rank = languageEntity.getRank();
        this.code = languageEntity.getCode();
        this.name = languageEntity.getName();
    }

}

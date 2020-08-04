package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.models.request.ReqTranslateItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsTranslates extends BaseEntity{

    private String languageCode;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;

    public ItemsTranslates(ReqTranslateItem requestData)
    {
        this.languageCode = requestData.getLanguageCode();
        this.name = requestData.getItemName();
        this.description = requestData.getDescription();
    }
}

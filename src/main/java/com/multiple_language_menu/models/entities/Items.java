package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.models.request.ReqCreateItem;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items extends BaseEntity{
    private String name;
    private BigDecimal price;
    private String description;
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Categories category;


    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private Collection<ItemsTranslates> itemsTranslates;

    @OneToMany(mappedBy = "item")
    private Collection<OrderDetails> orderDetails;

    public Items(ReqCreateItem reqCreateItem)
    {
        this.name = reqCreateItem.getItemName();
        this.price = reqCreateItem.getPrice();
        this.description = reqCreateItem.getDescription();
    }


}

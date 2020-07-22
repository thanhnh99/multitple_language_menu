package com.multiple_language_menu.models.entities;

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
    private Categories category;


    @OneToMany(mappedBy = "item")
    private Collection<ItemsTranslates> itemsTranslates;

    @OneToMany(mappedBy = "item")
    private Collection<OrderDetails> orderDetails;


}

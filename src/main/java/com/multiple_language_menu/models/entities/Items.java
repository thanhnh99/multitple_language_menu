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
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();
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

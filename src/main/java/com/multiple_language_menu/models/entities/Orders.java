package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    private String name;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shops shop;

    @OneToMany(mappedBy = "order")
    private Collection<OrderDetails> orderDetails;
}

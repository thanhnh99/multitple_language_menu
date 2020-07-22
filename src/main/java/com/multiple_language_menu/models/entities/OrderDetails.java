package com.multiple_language_menu.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails extends BaseEntity{

    @Column()
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;

}

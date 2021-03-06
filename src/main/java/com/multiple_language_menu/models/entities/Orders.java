package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity{

    private String name;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shops shop;

    @OneToMany(mappedBy = "order")
    private Collection<OrderDetails> orderDetails;

    public void addOrderDetail(OrderDetails orderDetail)
    {
        if(this.orderDetails == null)
        {
            this.orderDetails = new ArrayList<OrderDetails>();
        }
        else
        {
            this.orderDetails.add(orderDetail);
        }
    }
}

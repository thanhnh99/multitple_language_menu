package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments extends BaseEntity{

    private String name;
    private String code;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "payment_shop",
            joinColumns =  @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id")
    )
    @ToString.Exclude
    private List<Shops> shops = new ArrayList<>();

    public void addShop(Shops shops)
    {
        this.shops.add(shops);
    }
}

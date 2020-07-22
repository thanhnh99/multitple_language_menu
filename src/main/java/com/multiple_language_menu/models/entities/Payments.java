package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments extends BaseEntity{

    private String name;
    private String code;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Shops shop;
}

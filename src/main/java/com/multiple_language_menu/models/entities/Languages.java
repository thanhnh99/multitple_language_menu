package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Languages extends BaseEntity{
    private String code;
    private String name;
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @ToString.Exclude
    private Shops shop;

}

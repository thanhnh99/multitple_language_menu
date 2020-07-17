package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Languages extends BaseEntity{
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    private String languageCode;
    private String name;
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Shops shop;

}

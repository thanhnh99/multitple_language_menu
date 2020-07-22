package com.multiple_language_menu.models.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shops extends BaseEntity{

    private String name;
    private String address;
    private String phone;
    private String openTime;
    private String averagePrice;
    private String website;
    private Boolean wifi;
    private Integer parkingScale;
    private  Boolean smokingPlace;
    private  String coverImage;
    private String description;
    private Date contractTerm;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users owner;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Languages> languages;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Logs> logs;


    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Payments> payments;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Orders> orders;


}

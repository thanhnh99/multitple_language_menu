package com.multiple_language_menu.models.entities;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shops extends BaseEntity{

    private String name;
    private String address;
    private String phone;
    private String openTime;
    private String closeTime;
    private String website;
    private Boolean wifi;
    private Integer parkingScale;
    private Boolean preOrder;
    private  Boolean smokingPlace;
    private  String coverImage;
    private String description;
    private Date contractTerm;


    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "owner_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users owner;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Languages> languages;

    @OneToMany(mappedBy = "shop")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Logs> logs;


    @ManyToMany(mappedBy = "shops",  fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Payments> payments;

    @OneToMany(mappedBy = "shop",  fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Orders> orders;

    @OneToMany(mappedBy = "shop",  fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Categories> categories;


    public void addPayment(Payments payment)
    {
        this.payments.add(payment);
    }

    public Integer getAveragePrice()
    {
        BigDecimal averagePrice = new BigDecimal(0);
        BigDecimal sum = new BigDecimal(0);
        int itemCount = 0;
        List<Categories> categories = (List<Categories>) this.getCategories();
        for(Categories category : categories)
        {
            List<Items> items = (List<Items>) category.getItems();
            for (Items item : items)
            {
                itemCount++;
                sum.add(item.getPrice());
            }

        }
        if(sum.intValue() > 0 && itemCount >0)
        {
            return sum.intValue()/itemCount;
        }
        return 0;
    }


    public List<String> getPaymentMethod()
    {
        List<String> paymentMethod = new ArrayList<String>();
        for(Payments payment : this.getPayments())
        {
            paymentMethod.add(payment.getCode());
        }
        return paymentMethod;
    }


}

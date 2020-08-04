package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Shops;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResShop extends ResShopUser {
    private String ownerName;
    private String ownerPassword;
    private String ownerEmail;
    private String address;
    private String phone;
    private String averagePrice;
    private String website;
    private Date contractTerm;
    private List<String> paymentMethod;

    public ResShop(Shops shop)
    {
        super(shop);
        this.ownerName = shop.getOwner().getUsername();
        this.ownerPassword = shop.getOwner().getPassword();
        this.ownerEmail = shop.getOwner().getEmail();
        this.address = shop.getAddress();
        this.phone = shop.getPhone();
        this.averagePrice = shop.getAveragePrice().toString();
        this.website = shop.getWebsite();
        this.contractTerm = shop.getContractTerm();
        this.paymentMethod = shop.getPaymentMethod();
    }

}

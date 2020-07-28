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
public class ResShop {
    private String shopId;
    private String ownerName;
    private String ownerPassword;
    private String ownerEmail;
    private String shopName;
    private String address;
    private String phone;
    private String openTime;
    private String averagePrice;
    private String website;
    private Boolean wifi;
    private Integer parkingScale;
    private Boolean smokingPlace;
    private Boolean preOrder;
    private String description;
    private String coverImage;
    private Date contractTerm;
    private List<String> paymentMethod;

    public ResShop(Shops shop)
    {
        //TODO: convert shopEntity -> ResShop
        //Done
        this.shopId = shop.getId();
        this.ownerName = shop.getOwner().getUsername();
        this.ownerPassword = shop.getOwner().getPassword();
        this.ownerEmail = shop.getOwner().getEmail();
        this.shopName = shop.getName();
        this.address = shop.getAddress();
        this.phone = shop.getPhone();
        this.openTime = shop.getOpenTime();
        this.averagePrice = shop.getAveragePrice().toString();
        this.website = shop.getWebsite();
        this.wifi = shop.getWifi();
        this.parkingScale = shop.getParkingScale();
        this.smokingPlace = shop.getSmokingPlace();
        this.preOrder = shop.getPreOrder();
        this.description = shop.getDescription();
        this.coverImage = shop.getCoverImage();
        this.contractTerm = shop.getContractTerm();
        this.paymentMethod = shop.getPaymentMethod();
    }

}

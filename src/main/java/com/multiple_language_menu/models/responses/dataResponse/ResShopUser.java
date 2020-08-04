package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Shops;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResShopUser {
    private String shopName;
    private String shopId;
    private String openTime;
    private String closeTime;
    private Boolean wifi;
    private String website;
    private Integer parkingScale;
    private Boolean preOrder;
    private  Boolean smokingPlace;
    private  String coverImage;
    private String description;
    private String averagePrice;


    public ResShopUser(Shops shop)
    {
        this.shopName = shop.getName();
        this.shopId = shop.getId();
        this.openTime = shop.getOpenTime();
        this.closeTime = shop.getCloseTime();
        this.wifi = shop.getWifi();
        this.parkingScale = shop.getParkingScale();
        this.preOrder = shop.getPreOrder();
        this.smokingPlace = shop.getSmokingPlace();
        this.coverImage = shop.getCoverImage();
        this.description = shop.getDescription();
        this.website = shop.getWebsite();
        this.averagePrice = shop.getAveragePrice().toString();
    }
}

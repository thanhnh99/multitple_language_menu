package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqEditShop {
    private String ownerName;
    private String ownerPassword;
    private String ownerEmail;
    private String shopName;
    private String address;
    private String openTime;
    private String closeTime;
    private String website;
    private Boolean wifi;
    private Integer parkingScale;
    private Boolean smokingPlace;
    private Boolean preOrder;
    private String description;
    private String coverImage;
    private String contractTerm;
    private List<String> paymentMethod;
}

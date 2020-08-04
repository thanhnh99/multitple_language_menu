package com.multiple_language_menu.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateShop {
    private String ownerName;
    private String ownerPassword;
    private String ownerEmail;
    private String shopName;
    private String address;
    private String phone;
    private String openTime;
    private String closeTime;
    private String website;
    private Boolean wifi;
    private Integer parkingScale;
    private Boolean smokingPlace;
    private Boolean preOrder;
    private String description;
    private String coverImage;
    private Date contractTerm;
    private List<String> paymentMethod;
}

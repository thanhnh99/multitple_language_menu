package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.models.entities.Shops;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResShopUser {
    String shopName;
    String shopId;

    public ResShopUser(Shops shops)
    {
        this.shopName = shops.getName();
        this.shopId = shops.getId();
    }
}

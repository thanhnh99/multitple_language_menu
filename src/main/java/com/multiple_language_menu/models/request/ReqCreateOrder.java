package com.multiple_language_menu.models.request;

import lombok.Data;

import java.util.List;
@Data
public class ReqCreateOrder {
    private String shopId;
    private List<ReqItemData> itemList;
}

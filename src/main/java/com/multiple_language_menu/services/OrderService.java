package com.multiple_language_menu.services;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.ETargetType;
import com.multiple_language_menu.job.LogProcess;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.OrderDetails;
import com.multiple_language_menu.models.entities.Orders;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.models.request.ReqCreateOrder;
import com.multiple_language_menu.models.request.ReqItemData;
import com.multiple_language_menu.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    IShopRepository shopRepository;

    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    ILogRepository logRepository;

    @Autowired
    LogProcess logProcess;

    public boolean createOrder(ReqCreateOrder requestData)
    {
        try {
            Shops shop = shopRepository.findById(requestData.getShopId()).get();
            Orders newOrder = new Orders();
            newOrder.setShop(shop);
            orderRepository.save(newOrder);
            for(ReqItemData itemData : requestData.getItemList())
            {
                Items item = itemRepository.findById(itemData.getItemId()).get();
                OrderDetails newOrderDetail = new OrderDetails();
                newOrderDetail.setItem(item);
                newOrderDetail.setQuantity(itemData.getQuantity());
                newOrderDetail.setOrder(newOrder);
                orderDetailRepository.save(newOrderDetail);
            }
            ReqCreateLog reqCreateLog = new ReqCreateLog();
            reqCreateLog.setActionType(EActionType.ORDER);
            reqCreateLog.setShopId(shop.getId());
            reqCreateLog.setTargetId(newOrder.getId());
            reqCreateLog.setTargetName(newOrder.getName());
            reqCreateLog.setTargetType(ETargetType.ITEM);
            logProcess.createLog(reqCreateLog, shopRepository, logRepository);
            return true;
        } catch (Exception e)
        {
            System.out.println("Err in OrderService.CreateOrder: "+ e.getMessage());
            return false;
        }
    }
}

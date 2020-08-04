package com.multiple_language_menu.services;

import com.multiple_language_menu.job.TranslateProcess;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateItem;
import com.multiple_language_menu.models.request.ReqEditItem;
import com.multiple_language_menu.repositories.*;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IItemTranslateRepository itemTranslateRepository;

    @Autowired
    IShopRepository shopRepository;

    @Autowired
    ICategoryRepository categoryRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    TranslateProcess translateProcess;
    public Boolean createItem(HttpServletRequest httpRequest, ReqCreateItem requestData)
    {
        try {
            String token = httpRequest.getHeader("Authorization");
            Categories category = categoryRepository.getOne(requestData.getCategoryId());
            if(!category.getShop().getOwner().getEnable())
            {
                return false;
            }
            Users manager = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            //check access
            if(AttributeTokenService.checkAccess(token,"manager") &&
                    manager.getId().equals(category.getShop().getOwner().getId()))
            {
                Items newItem = new Items(requestData);
                newItem.setCategory(category);
                newItem.setRank(category.getItems().size());
                itemRepository.save(newItem);
                //Todo translate schedule
                translateProcess.translateItem(newItem, itemTranslateRepository);
                return true;
            }
            return false;
        }catch (Exception e)
        {
            System.out.println("Err in ItemService.createItem: " + e.getMessage());
            return false;
        }
    }


    public Boolean editItem(HttpServletRequest httpRequest, ReqEditItem requestData)
    {
        try {
            String itemId = new String();//
            if(requestData.getItemIds() instanceof  List)
            {
                //Todo update Rank
            }
            else if(requestData.getItemIds() instanceof String)
            {
                //
            }
            Items  item = itemRepository.findById(itemId).get();
            if(item == null || !item.getCategory().getShop().getOwner().getEnable())
            {
                return false;
            }
            //check Access
            String token = httpRequest.getHeader("Authorization");
            Boolean checkAccess = false;
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            if(AttributeTokenService.checkAccess(token,"root"))
            {
                checkAccess = true;
            }
            else if(AttributeTokenService.checkAccess(token,"admin"))
            {
                //check CreateBY
                if(user.getId().equals(item.getCategory().getShop().getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else if(AttributeTokenService.checkAccess(token,"manager"))
            {
                //check ownerId
                if(user.getId().equals(item.getCategory().getShop().getOwner().getId()))
                {
                    checkAccess = true;
                }
            }

            if(checkAccess == true)
            {
                //Todo update logic
                return true;
            }

            return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.deleteCategory: " + e.getMessage());
            return false;
        }    }

    public Boolean deleteItem(HttpServletRequest httpRequest, String itemId)
    {
        try {
            //check Access
            Items item = itemRepository.getOne(itemId);
            if(item == null || !item.getCategory().getShop().getOwner().getEnable())
            {
                return false;
            }
            //check Access
            String token = httpRequest.getHeader("Authorization");
            Boolean checkAccess = false;
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            if(AttributeTokenService.checkAccess(token,"manager"))
            {
                //check Owner
                if(user.getId().equals(item.getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else {
                checkAccess = false;
            }
            if(checkAccess)
            {
                itemRepository.delete(item);
                return true;
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.deleteCategory: " + e.getMessage());
            return false;
        }
    }

}

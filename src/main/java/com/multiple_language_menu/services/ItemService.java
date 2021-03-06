package com.multiple_language_menu.services;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.RoleConstant;
import com.multiple_language_menu.job.LogProcess;
import com.multiple_language_menu.job.TranslateProcess;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateItem;
import com.multiple_language_menu.models.request.ReqCreateItemFromCSV;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.models.request.ReqEditItem;
import com.multiple_language_menu.repositories.*;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    ILogRepository logRepository;

    @Autowired
    TranslateProcess translateProcess;

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    CategoryService categoryService;

    @Autowired
    LogProcess logProcess;

    public Boolean createItem(HttpServletRequest httpRequest, ReqCreateItem requestData)
    {
        try {
            String token = httpRequest.getHeader("Authorization");
            Categories category = categoryRepository.getOne(requestData.getCategoryId());
            if(!category.getShop().getOwner().getEnable() ||
                    category.getChildCategory().size() > 0 ||
                    category == null
            )
            {
                return false;
            }
            Users manager = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            //check access
            if(AttributeTokenService.checkAccess(token, RoleConstant.MANAGER) &&
                    manager.getId().equals(category.getShop().getOwner().getId()))
            {
                Items newItem = new Items(requestData);
                newItem.setCategory(category);
                newItem.setRank(category.getItems().size());
                newItem.setCreatedBy(manager.getId());
                if(requestData instanceof ReqCreateItemFromCSV)
                {
                    newItem.setId(((ReqCreateItemFromCSV) requestData).getCode() + category.getId());
                }
                itemRepository.save(newItem);
                category.getItems().add(newItem);
                category.setItems(category.getItems());
                categoryRepository.save(category);

                logProcess.createLog(new ReqCreateLog(newItem, EActionType.ADD),
                        shopRepository,
                        logRepository);

                translateProcess.translateItem(newItem,
                        itemTranslateRepository,
                        shopRepository,
                        logRepository,
                        emailSender);
                return true;
            }
            return false;
        }catch (Exception e)
        {
            System.out.println("Err in ItemService.createItem: " + e.getMessage());
            return false;
        }
    }


    public Boolean editItem(HttpServletRequest httpRequest, ReqEditItem requestData) {
        try {
            String token = httpRequest.getHeader("Authorization");
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            if (requestData.getItemIds() instanceof List) {
                //update Rank
                Boolean checkAccess = false;
                for (String itemUpdateId : (List<String>) requestData.getItemIds()) {
                    Items itemUpdate = itemRepository.findById(itemUpdateId).get();
                    if (AttributeTokenService.checkAccess(token, RoleConstant.ROOT)
                            || (AttributeTokenService.checkAccess(token, RoleConstant.MANAGER) &&
                            user.getId().equals(itemUpdate.getCategory().getShop().getOwner().getId()))) {
                        checkAccess = true;
                    }
                    else {
                        checkAccess = false;
                    }
                    if (checkAccess == false) break;
                }
                if(checkAccess == true)
                {
                    int index = 0;
                    for (String itemUpdateId : (List<String>) requestData.getItemIds())
                    {
                        Items itemUpdate = itemRepository.findById(itemUpdateId).get();
                        itemUpdate.setRank(index++);
                        itemRepository.save(itemUpdate);
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            } else if (requestData.getItemIds() instanceof String) {
                //update data
                Items updateItem = itemRepository.findById((String) requestData.getItemIds()).get();
                if (AttributeTokenService.checkAccess(token, RoleConstant.ROOT)
                        || (AttributeTokenService.checkAccess(token, RoleConstant.MANAGER) &&
                        user.getId().equals(updateItem.getCategory().getShop().getOwner().getId()))) {
                    updateItem.setName(requestData.getItemName());
                    updateItem.setDescription(requestData.getDescription());
                    updateItem.setUpdatedAt(new Date());
                    updateItem.setUpdatedBy(user.getId());
                    if (requestData.getCategoryId() != null && !requestData.getCategoryId().equals(updateItem.getCategory().getId())) {
                        Categories toCategory = categoryRepository.findById(requestData.getCategoryId()).get();
                        List<Categories> childCategories = categoryRepository.findCategoriesByCategoriesParent(toCategory);
                        if (toCategory != null && childCategories.size() < 1) {
                            updateItem.setCategory(toCategory);
                        }
                    }
                    itemRepository.save(updateItem);
                    // update translate data.
                    translateProcess.translateItem(updateItem,
                            itemTranslateRepository,
                            shopRepository,
                            logRepository,
                            emailSender);
                    return true;
                }
                return false;
            }
        }catch (Exception e)
        {
            System.out.println("Err in CategoryService.deleteCategory: " + e.getMessage());
            return false;
        }
        return false;
    }

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
            if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT) ||
                    (AttributeTokenService.checkAccess(token,RoleConstant.MANAGER) &&
                            user.getId().equals(item.getCreatedBy()))
            )
            {
                checkAccess = true;
            }
            else {
                checkAccess = false;
            }
            if(checkAccess)
            {
                itemRepository.delete(item);
                logProcess.createLog(new ReqCreateLog(item, EActionType.DELETE),
                        shopRepository,
                        logRepository);
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

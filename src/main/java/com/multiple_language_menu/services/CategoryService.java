package com.multiple_language_menu.services;

import com.multiple_language_menu.job.TranslateCategoryJob;
import com.multiple_language_menu.job.TranslateProcess;
import com.multiple_language_menu.models.entities.*;
import com.multiple_language_menu.models.request.ReqCreateCategory;
import com.multiple_language_menu.models.request.ReqEditCategory;
import com.multiple_language_menu.models.responses.dataResponse.ResCategory;
import com.multiple_language_menu.models.responses.dataResponse.ResItem;
import com.multiple_language_menu.repositories.*;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    IShopRepository shopRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ICategoryRepository categoryRepository;

    @Autowired
    ICategoryTranslateRepository categoryTranslateRepository;

    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IItemTranslateRepository itemTranslateRepository;

    @Autowired
    TranslateProcess translateProcess;


    public Boolean createCategory(HttpServletRequest httpRequest, ReqCreateCategory requestData)
    {
        try {
            String token = httpRequest.getHeader("Authorization");
            Shops shop = shopRepository.findById(requestData.getShopId()).get();
            if(!shop.getOwner().getEnable() || shop == null)
            {
                return null;
            }
            Users manager = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            Categories parentCategory = null;
            if(requestData.getParentCategory() != null)
            {
                parentCategory = categoryRepository.findById(requestData.getParentCategory()).get();
            }
            if(true)
            {
                Categories newCategory = new Categories();
                newCategory.setName(requestData.getCategoryName());
                newCategory.setDescription(requestData.getDescription());
                newCategory.setShop(shop);
                newCategory.setCreatedBy(manager.getId());
                newCategory.setCategoriesParent(parentCategory);
                categoryRepository.save(newCategory);

                // translate schedule
                translateProcess.translateCategory(newCategory, categoryTranslateRepository);
                return true;
            }
                return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.create: " + e.getMessage());
            return false;
        }
    }


    public Boolean editCategory(HttpServletRequest httpRequest,
                                ReqEditCategory requestData)
    {
        try {
            //check Access
            String token = httpRequest.getHeader("Authorization");
            Boolean checkAccess = false;
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            //Update
            if(requestData.getCategoryIds() instanceof  List) //update rank
            {
                //Todo update Rank
                for (String categoryUpdateId : (List<String>) requestData.getCategoryIds())
                {
                    Categories categoryUpdate = categoryRepository.findById(categoryUpdateId).get();
                    if(!categoryUpdate.getCategoriesParent().getId().equals(requestData.getParentId())||
                            !AttributeTokenService.checkAccess(token,"root")
                            ||!(AttributeTokenService.checkAccess(token,"manager")&&
                            user.getId().equals(categoryUpdate.getShop().getOwner().getId())))
                    {
                        return false;
                    }
                }
                int index = 1;
                for (String categoryUpdateId : (List<String>) requestData.getCategoryIds())
                {
                    Categories categoryUpdate = categoryRepository.findById(categoryUpdateId).get();
                    categoryUpdate.setRank(index++);
                    categoryRepository.save(categoryUpdate);
                }
            }
            else if(requestData.getCategoryIds() instanceof  String) //update data
            {
                Categories updateCategory = categoryRepository.findById((String) requestData.getCategoryIds()).get();
                if(AttributeTokenService.checkAccess(token,"root")
                        ||(AttributeTokenService.checkAccess(token,"manager")&&
                        user.getId().equals(updateCategory.getShop().getOwner().getId())))
                {
                    updateCategory.setName(requestData.getCategoryName());
                    updateCategory.setDescription(requestData.getDescription());
                    if(requestData.getParentId() == null)
                    {
                        Categories parentCategory = categoryRepository.findById(requestData.getParentId()).get();
                        List<Categories> childCategories = categoryRepository.findCategoriesByCategoriesParent(parentCategory);
                        updateCategory.setCategoriesParent(parentCategory);
                        updateCategory.setRank(childCategories.size());
                    }
                    categoryRepository.save(updateCategory);
                    //Todo update translate data.
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.deleteCategory: " + e.getMessage());
            return false;
        }
    }

    public Boolean deleteCategory(HttpServletRequest httpRequest, String categoryId)
    {
        try {
            //check Access
            Categories category = categoryRepository.getOne(categoryId);
            if(category == null || !category.getShop().getOwner().getEnable())
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
                if(user.getId().equals(category.getShop().getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else if(AttributeTokenService.checkAccess(token,"manager"))
            {
                //check Owner
                if(user.getId().equals(category.getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else {
                checkAccess = false;
            }
            if(checkAccess)
            {
                categoryRepository.delete(category);
                return true;
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.deleteCategory: " + e.getMessage());
            return false;
        }
    }

    public List<ResCategory> getCategories(HttpServletRequest httpRequest)
    {
        String page = httpRequest.getParameter("page");
        String pagesize = httpRequest.getParameter("pagesize");
        String shopId = httpRequest.getParameter("shop_id");
        Shops shop = shopRepository.getOne(shopId);
        if(shop == null || !shop.getOwner().getEnable())
        {
            return null;
        }
        String languageCode = httpRequest.getParameter("language_code");
        if(page== null || pagesize== null || shopId == null)
        {
            return null;
        }
        int pageInt = Integer.parseInt(page);
        int pagesizeInt = Integer.parseInt(pagesize);
        if(languageCode == null)
        {
            //Todo: get from category
//            List<Categories> categories = categoryRepository.findCategoriesByShop(sho)
        }
        else
        {
            //Todo: get from category translate;


        }
        return null;
    }

    public List<Object> getChildByCategoryId(String categoryId, String languageCode)
    {

        try{
            List<Object> response = new ArrayList<Object>();
            Categories categoryParent = categoryRepository.getOne(categoryId);
            List<Categories> categories = categoryRepository.findCategoriesByCategoriesParent(categoryParent);
            List<Items> items = itemRepository.findByCategory(categoryParent);
            if(categories.size() > 0)
            {
                for(Categories category : categories)
                {
                    CategoriesTranslates categoryTranslate = categoryTranslateRepository.findByCategoryAndLanguageCode(category,languageCode);
                    ResCategory  resCategory = new ResCategory(categoryTranslate);
                    response.add(resCategory);
                }
            }
            else if(items.size() > 0)
            {
                for(Items item : items)
                {
                    ItemsTranslates itemTranslates = itemTranslateRepository.findByItemAndLanguageCode(item,languageCode);
                    ResItem resCategory = new ResItem(itemTranslates);
                    response.add(resCategory);
                }
            }
            return response;
        }catch (Exception e)
        {
            System.out.println("Err in CategoryService.getChildByCategoryId: " + e.getMessage());
            return null;
        }
    }
}

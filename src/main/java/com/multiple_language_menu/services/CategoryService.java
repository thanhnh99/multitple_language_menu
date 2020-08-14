package com.multiple_language_menu.services;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.RoleConstant;
import com.multiple_language_menu.job.LogProcess;
import com.multiple_language_menu.job.TranslateProcess;
import com.multiple_language_menu.models.entities.*;
import com.multiple_language_menu.models.request.*;
import com.multiple_language_menu.models.responses.dataResponse.ResCategory;
import com.multiple_language_menu.models.responses.dataResponse.ResItem;
import com.multiple_language_menu.repositories.*;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
    ILogRepository logRepository;

    @Autowired
    TranslateProcess translateProcess;

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    ItemService itemService;

    @Autowired
    LogProcess logProcess;


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
            if(requestData.getParentCategory() != null && !requestData.getParentCategory().equals(""))
            {
                parentCategory = categoryRepository.findById(requestData.getParentCategory()).get();
                if(parentCategory.getCategoriesParent() != null ||parentCategory.getItems().size() > 0)
                {
                    return false;
                }
            }
            Categories newCategory = new Categories();
            newCategory.setName(requestData.getCategoryName());
            newCategory.setDescription(requestData.getDescription());
            newCategory.setShop(shop);
            newCategory.setCreatedBy(manager.getId());
            if(requestData instanceof ReqCreateCategoryFromCSV)
            {
                newCategory.setId(((ReqCreateCategoryFromCSV) requestData).getCode());
            }
            newCategory.setCategoriesParent(parentCategory);
            if(parentCategory != null)
            {
                newCategory.setRank(parentCategory.getChildCategory().size());
                parentCategory.getChildCategory().add(newCategory);
                parentCategory.setChildCategory(parentCategory.getChildCategory());
                categoryRepository.save(parentCategory);
            }
            else
            {
                newCategory.setRank(shop.getCategories().size());
                shop.getCategories().add(newCategory);
                shop.setCategories(shop.getCategories());
            }
            categoryRepository.save(newCategory);

            // Create category Log
            logProcess.createLog(new ReqCreateLog(newCategory, EActionType.ADD),
                    shopRepository,
                    logRepository);

            // translate schedule
            translateProcess.translateCategory(newCategory,
                    categoryTranslateRepository,
                    shopRepository,
                    logRepository,
                    emailSender);
            return true;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.create: " + e.getMessage());
            return false;
        }
    }


    public boolean uploadCSV(HttpServletRequest httpRequest, MultipartFile file)
    {
        try {
            Users users = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(httpRequest.getHeader("Authorization")));
            Shops shops = shopRepository.findByOwner(users);
            if(shops.getCategories().size() == 0)
            {
                InputStream inputStream = file.getInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();
                Integer index =1;
                while (rowIterator.hasNext())
                {
                    System.out.println(rowIterator.hasNext());


                    Cell type = sheet.getRow(index).getCell(0);
                    Cell parent = sheet.getRow(index).getCell(1);
                    Cell code = sheet.getRow(index).getCell(2);
                    Cell name = sheet.getRow(index).getCell(3);
                    Cell description = sheet.getRow(index).getCell(4);
                    Cell price = sheet.getRow(index).getCell(5);

                    if(type.getNumericCellValue() == 1)
                    {
                        System.out.println("Create Category");
                        ReqCreateCategoryFromCSV reqCreateCategory = new ReqCreateCategoryFromCSV();
                        reqCreateCategory.setCode(code.getStringCellValue() + shops.getId());
                        reqCreateCategory.setCategoryName(name.getStringCellValue());
                        reqCreateCategory.setParentCategory(parent.getStringCellValue());
                        reqCreateCategory.setShopId(shops.getId());
                        reqCreateCategory.setDescription(description.getStringCellValue());
                        this.createCategory(httpRequest,reqCreateCategory);
                    }
                    else if(type.getNumericCellValue() == 2)
                    {
                        ReqCreateItemFromCSV reqCreateItem = new ReqCreateItemFromCSV();
                        reqCreateItem.setCategoryId(parent.getStringCellValue() + shops.getId());
                        reqCreateItem.setDescription(description.getStringCellValue());
                        reqCreateItem.setCode(code.getStringCellValue());
                        reqCreateItem.setItemName(name.getStringCellValue());
                        reqCreateItem.setPrice(new BigDecimal(price.getNumericCellValue()));
                        itemService.createItem(httpRequest, reqCreateItem);
                        System.out.println("Create Item");
                    }
                    else
                    {
                        System.out.println("Err readFile");
                        break;
                    }
                    index++;
                }
                return true;
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.readDataByFile: " + e.getMessage());
            return true;
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
                // update Rank
                for (String categoryUpdateId : (List<String>) requestData.getCategoryIds())
                {
                    Categories categoryUpdate = categoryRepository.findById(categoryUpdateId).get();
                    if(AttributeTokenService.checkAccess(token, RoleConstant.ROOT)
                            ||(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER)&&
                            user.getId().equals(categoryUpdate.getShop().getOwner().getId())))
                    {
                        checkAccess = true;
                    }
                    else
                    {
                        checkAccess = false;
                    }
                }
                if (checkAccess)
                {
                    int index = 0;
                    for (String categoryUpdateId : (List<String>) requestData.getCategoryIds())
                    {
                        Categories categoryUpdate = categoryRepository.findById(categoryUpdateId).get();
                        categoryUpdate.setRank(index++);
                        categoryRepository.save(categoryUpdate);
                    }
                    logProcess.createLog(new ReqCreateLog(categoryRepository.findById(((List<String>) requestData.getCategoryIds()).get(0)).get(), EActionType.UPDATE_RANK),
                            shopRepository,
                            logRepository);
                    return true;
                }
            }
            else if(requestData.getCategoryIds() instanceof  String) //update data
            {
                Categories updateCategory = categoryRepository.findById((String) requestData.getCategoryIds()).get();
                if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT)
                        ||(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER)&&
                        user.getId().equals(updateCategory.getShop().getOwner().getId())))
                {
                    updateCategory.setName(requestData.getCategoryName());
                    updateCategory.setDescription(requestData.getDescription());
                    if(requestData.getParentId() != null)
                    {
                        Categories parentCategory = categoryRepository.findById(requestData.getParentId()).get();
                        List<Categories> childCategories = categoryRepository.findCategoriesByCategoriesParent(parentCategory);
                        updateCategory.setCategoriesParent(parentCategory);
                        updateCategory.setRank(childCategories.size());
                    }
                    categoryRepository.save(updateCategory);

                    //Create log
                    logProcess.createLog(new ReqCreateLog(updateCategory, EActionType.UPDATE_DATA),
                            shopRepository,
                            logRepository);

                    // update translate data.
                    translateProcess.translateCategory(updateCategory,
                            categoryTranslateRepository,
                            shopRepository,
                            logRepository,
                            emailSender);
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
            Categories category = categoryRepository.findById(categoryId).get();
            if(category == null || !category.getShop().getOwner().getEnable())
            {
                return false;
            }
            //check Access
            String token = httpRequest.getHeader("Authorization");
            Boolean checkAccess = false;
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT))
            {
                checkAccess = true;
            }
            else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
            {
                //check CreateBY
                if(user.getId().equals(category.getShop().getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
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
                //Create log
                logProcess.createLog(new ReqCreateLog(category, EActionType.DELETE),
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

    public List<ResCategory> getCategoryByShopId(HttpServletRequest httpRequest)
    {
        try {
            String shopId = httpRequest.getParameter("shop_id");
            Shops shop = shopRepository.findById(shopId).get();
            if(shop == null || !shop.getOwner().getEnable())
            {
                return null;
            }
            String languageCode = httpRequest.getParameter("language_code");
            List<ResCategory> responses = new ArrayList<ResCategory>();
            if(languageCode == null)
            {
                List<Categories> categories = categoryRepository.findCategoriesByShop(shop);
                for(Categories category : categories)
                {
                    if(category.getCategoriesParent() == null)
                    {
                        ResCategory resCategory = new ResCategory(category);
                        responses.add(resCategory);
                    }
                }

            }
            else
            {
                List<Categories> categories = categoryRepository.findCategoriesByShop(shop);
                for(Categories category : categories)
                {
                    if(category.getCategoriesParent() == null)
                    {
                        CategoriesTranslates categoriesTranslates = categoryTranslateRepository.findByCategoryAndLanguageCode(category,languageCode);
                        ResCategory resCategory;
                        if(categoriesTranslates == null)
                        {
                            resCategory = new ResCategory(category);
                            translateProcess.translateCategory(category,
                                    categoryTranslateRepository,
                                    shopRepository,
                                    logRepository,
                                    emailSender);

                        }
                        else
                        {
                            resCategory = new ResCategory(categoriesTranslates);
                        }
                        responses.add(resCategory);
                    }
                }

            }
            return responses;
        } catch (Exception e)
        {
            System.out.println("Err in CategoryService.getCategories: " + e.getMessage());
            return null;
        }
    }

    public List<Object> getChildByCategoryId(String categoryId, String languageCode)
    {

        try{
            List<Object> response = new ArrayList<Object>();
            Categories categoryParent = categoryRepository.findById(categoryId).get();
            List<Categories> childCategories = (List<Categories>) categoryParent.getChildCategory();
            List<Items> items = (List<Items>) categoryParent.getItems();
            if(childCategories.size() > 0)
            {
                for(Categories category : childCategories)
                {
                    ResCategory  resCategory = null;
                    CategoriesTranslates categoryTranslate = categoryTranslateRepository.findByCategoryAndLanguageCode(category,languageCode);
                    if(categoryTranslate != null)
                    {
                        resCategory = new ResCategory(categoryTranslate);
                    }
                    else
                    {
                        resCategory = new ResCategory(category);
                        translateProcess.translateCategory(category,
                                categoryTranslateRepository,
                                shopRepository,
                                logRepository,
                                emailSender);
                    }
                    response.add(resCategory);
                }
            }
            if(items.size() > 0)
            {
                for(Items item : items)
                {
                    ResItem resItem = null;
                    ItemsTranslates itemTranslates = itemTranslateRepository.findByItemAndLanguageCode(item,languageCode);
                    if(itemTranslates != null)
                    {
                        resItem = new ResItem(itemTranslates);
                    }
                    else
                    {
                        resItem = new ResItem(item);
                        translateProcess.translateItem(item,
                                itemTranslateRepository,
                                shopRepository,
                                logRepository,
                                emailSender);
                    }
                    response.add(resItem);
                }
            }
            return response;
        }catch (Exception e)
        {
            System.out.println("Err in CategoryService.getChildByCategoryId: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

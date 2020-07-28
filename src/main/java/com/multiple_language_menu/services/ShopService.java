package com.multiple_language_menu.services;

import com.multiple_language_menu.models.entities.Payments;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.models.responses.dataResponse.ResShop;
import com.multiple_language_menu.repositories.IPaymentRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopService {
    @Autowired
    UserService userService;
    @Autowired
    LanguageService languageService;


    @Autowired
    IUserRepository userRepository;
    @Autowired
    IPaymentRepository paymentRepository;
    @Autowired
    IShopRepository shopRepository;

    public Boolean createShop(HttpServletRequest httpRequest, ReqCreateShop requestData)
    {
        Users manager = null;
        try {
            //check permission
            //if has permission => create shop and return data
            String token = httpRequest.getHeader("Authorization");
            if(AttributeTokenService.checkAccess(token,"root")||
                    AttributeTokenService.checkAccess(token,"admin"))
            {
                //create shop manager
                ReqCreateAdmin reqCreateAdmin = new ReqCreateAdmin(requestData.getOwnerName(),
                        requestData.getOwnerPassword(),
                        requestData.getOwnerEmail());
                manager = userService.createManager(httpRequest, reqCreateAdmin);

                if(manager != null)
                {
                    //getPaymentMethod in DB
                    List<Payments> payments = new ArrayList<>();
                    for(String paymentCode : requestData.getPaymentMethod())
                    {
                        Payments payment = paymentRepository.findByCode(paymentCode);
                        payments.add(payment);
                    }
                    //create shop
                    Shops shop = new Shops(requestData.getShopName(),
                            requestData.getAddress(),
                            requestData.getPhone(),
                            requestData.getOpenTime(),
                            requestData.getWebsite(),
                            requestData.getWifi(),
                            requestData.getParkingScale(),
                            requestData.getPreOrder(),
                            requestData.getSmokingPlace(),
                            requestData.getCoverImage(),
                            requestData.getDescription(),
                            requestData.getContractTerm(),
                            manager,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            payments,
                            new ArrayList<>(),
                            new ArrayList<>());
                    shop.setLanguages(languageService.getLanguages());
                    shop.setCreated_by(manager.getCreated_by());
                    shopRepository.save(shop);
                    languageService.addLanguage(shop.getId());
                    return true;
                }
            }
                else
                {
                    userRepository.delete(manager);
                    return  false;
                }

        } catch (Exception e)
        {
            System.out.println("Err in ShopService.CreateShop: " + e.getMessage());
            userRepository.delete(manager);
            return false;
        }
        //else return null
        return false;
    }


    public List<ResShop> getShops(HttpServletRequest httpRequest)
    {
        //TODO: getShop logic
        //Done
        try {
            String page = httpRequest.getParameter("page");
            String pagesize = httpRequest.getParameter("pagesize");
            String startDate = httpRequest.getParameter("startDate");
            String endDate = httpRequest.getParameter("endDate");

            List<ResShop> response = new ArrayList<ResShop>();
            String token = httpRequest.getHeader("Authorization");
            String username = AttributeTokenService.getUsernameFromToken(token);

            if(page == null || pagesize == null)
            {
                //Err = pagesize and page is required fields
                return null;
            }
//            else if(startDate == null || endDate == null)
//            {
//                if(AttributeTokenService.checkAccess(token,"root"))
//                {
//                    List<Shops> shops = shopRepository.findAll();
//                    for(Shops shopEntity : shops)
//                    {
//                        ResShop resShop = new ResShop(shopEntity);
//                        response.add(resShop);
//                    }
//                }
//                else if(AttributeTokenService.checkAccess(token,"admin"))
//                {
//                    Users admin = userRepository.findByUsername(username);
//                    List<Shops> shops = shopRepository.findByCreatedById(admin.getId());
//                    for(Shops shopEntity : shops)
//                    {
//                        ResShop resShop = new ResShop(shopEntity);
//                        response.add(resShop);
//                    }
//                }
//                else if(AttributeTokenService.checkAccess(token,"manager"))
//                {
//                    Users manager = userRepository.findByUsername(username);
//                    List<Shops> shops = shopRepository.findByOwnerId(manager.getId());
//                    for(Shops shopEntity : shops)
//                    {
//                        ResShop resShop = new ResShop(shopEntity);
//                        response.add(resShop);
//                    }
//                }
//                else
//                {
//                    Date startDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
//                    Date endDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
//                    if(AttributeTokenService.checkAccess(token,"root"))
//                    {
//                        List<Shops> shops = shopRepository.findBetweenContractTerm(startDateFind, endDateFind);
//                        for(Shops shopEntity : shops)
//                        {
//                            ResShop resShop = new ResShop(shopEntity);
//                            response.add(resShop);
//                        }
//                    }
//                    else if(AttributeTokenService.checkAccess(token,"admin"))
//                    {
//                        Users admin = userRepository.findByUsername(username);
//                        List<Shops> shops = shopRepository
//                                        .findByCreatedByIdAndBetweenContractTerm
//                                        (admin.getId(),startDateFind,endDateFind);
//                        for(Shops shopEntity : shops)
//                        {
//                            ResShop resShop = new ResShop(shopEntity);
//                            response.add(resShop);
//                        }
//                    }
//                    else if(AttributeTokenService.checkAccess(token,"manager"))
//                    {
//                        Users manager = userRepository.findByUsername(username);
//                        List<Shops> shops = shopRepository.findByOwnerId(manager.getId());
//                        for(Shops shopEntity : shops)
//                        {
//                            ResShop resShop = new ResShop(shopEntity);
//                            response.add(resShop);
//                        }
//                    }
//                }
//            }
        else
            {

            }
            return response;
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.getShops: " + e.getMessage());
            return null;
        }
    }
}

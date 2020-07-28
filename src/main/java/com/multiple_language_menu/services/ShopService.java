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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
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
                    shop.setCreatedBy(manager.getCreatedBy());
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
            Page<Shops> shopsEntity = null;
            String token = httpRequest.getHeader("Authorization");
            String username = AttributeTokenService.getUsernameFromToken(token);

            if(page == null || pagesize == null)
            {
                //Err = pagesize and page is required fields
                return null;
            }
            else
            {
                int pageInt = Integer.parseInt(page);
                int pagesizeInt = Integer.parseInt(pagesize);
                if(startDate == null && endDate == null)
                {
                    if(AttributeTokenService.checkAccess(token,"root"))
                    {
                        shopsEntity = shopRepository.findAll(PageRequest.of(pageInt,pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,"admin"))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedBy(admin.getId(), PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,"manager"))
                    {
                        Users manager = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByOwner(manager, PageRequest.of(pageInt, pagesizeInt));
                    }
                    else {

                    }
                }
                else if(startDate != null && endDate ==null)
                {
                    Date startDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                    if(AttributeTokenService.checkAccess(token,"root"))
                    {
                        shopsEntity = shopRepository.findByContractTermAfter(startDateFind, PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,"admin"))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedByAndContractTermAfter
                                        (admin.getId(),startDateFind,  PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,"manager"))
                    {
                        Users manager = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByOwnerAndContractTermAfter
                                        (manager,startDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else
                    {

                    }
                }
                else if(startDate == null && endDate != null)
                {
                    Date endDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                    if(AttributeTokenService.checkAccess(token,"root"))
                    {
                        shopsEntity = shopRepository.findByContractTermBefore(endDateFind, PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,"admin"))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedByAndContractTermBefore
                                        (admin.getId(),endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else if(AttributeTokenService.checkAccess(token,"manager"))
                    {
                        Users manager = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByOwnerAndContractTermBefore
                                        (manager,endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else
                    {

                    }
                }
                else
                {
                    Date startDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                    Date endDateFind = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                    if(AttributeTokenService.checkAccess(token,"root"))
                    {
                        shopsEntity = shopRepository.findByContractTermBetween(startDateFind, endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else if(AttributeTokenService.checkAccess(token,"admin"))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository
                                        .findByCreatedByAndContractTermBetween
                                        (admin.getId(),startDateFind,endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else if(AttributeTokenService.checkAccess(token,"manager"))
                    {
                        Users manager = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByOwnerAndContractTermBetween(
                                        manager,
                                        startDateFind,
                                        endDateFind,
                                        PageRequest.of(pageInt, pagesizeInt)
                                );
                    }
                }
                if(shopsEntity != null)
                {
                    for(Shops shop : shopsEntity)
                    {
                        ResShop resShop = new ResShop(shop);
                        response.add(resShop);
                    }
                }
                else return null;
            }
            return response;
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.getShops: " + e.getMessage());
            return null;
        }
    }



}

package com.multiple_language_menu.services;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.RoleConstant;
import com.multiple_language_menu.job.LogProcess;
import com.multiple_language_menu.models.entities.Payments;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.models.request.ReqEditShop;
import com.multiple_language_menu.models.responses.dataResponse.ResShop;
import com.multiple_language_menu.models.responses.dataResponse.ResShopUser;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IPaymentRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
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
    @Autowired
    ILogRepository logRepository;
    @Autowired
    LogProcess logProcess;

    @Autowired
    PasswordEncoder passwordEncoder;

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
                        if(payment != null)
                        {
                            payments.add(payment);
                        }
                    }
                    //create shop
                    Shops shop = new Shops(requestData.getShopName(),
                            requestData.getAddress(),
                            requestData.getPhone(),
                            requestData.getOpenTime(),
                            requestData.getCloseTime(),
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
                    shop.setLanguages(new ArrayList<>());
                    shop.setCreatedBy(manager.getCreatedBy());
                    shopRepository.save(shop);
                    for(String paymentCode : requestData.getPaymentMethod())
                    {
                        Payments payment = paymentRepository.findByCode(paymentCode);
                        if(payment != null)
                        {
                            payment.addShop(shop);
                            paymentRepository.save(payment);
                        }
                    }
                    languageService.addDefaultLanguage(shop.getId());

                    //Create log
                    logProcess.createLog(new ReqCreateLog(shop, EActionType.ADD),
                            shopRepository,
                            logRepository);
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


    public Boolean uploadCoverImage(HttpServletRequest httpRequest,
                                    String shopId,
                                    MultipartFile coverImage)
    {
        try {
            boolean checkAccess = false;
            String token = httpRequest.getHeader("Authorization");
            Shops shop = shopRepository.findById(shopId).get();
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            if (AttributeTokenService.checkAccess(token, RoleConstant.ROOT) ||
                    (AttributeTokenService.checkAccess(token, RoleConstant.MANAGER) &&
                            shop.getOwner().getId().equals(user.getId())))
            {
                checkAccess = true;
            }
            if(checkAccess)
            {
                Path root = Paths.get("uploads");

//                Files.createDirectory(root);
                Files.copy(coverImage.getInputStream(), root.resolve(System.currentTimeMillis() + coverImage.getOriginalFilename()));
                String filename = new String();
                Path file = root.resolve(filename);
                UrlResource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    String coverLink = resource.getURL().toString();
                    shop.setCoverImage(coverLink);
                    shopRepository.save(shop);
                    logProcess.createLog(new ReqCreateLog(shop, EActionType.UPLOAD_COVER_IMAGE),
                            shopRepository,
                            logRepository);
                    return true;
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.uploadCoverImage");
            return false;
        }
    }


    public List<ResShop> getShops(HttpServletRequest httpRequest)
    {
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
                    if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT))
                    {
                        shopsEntity = shopRepository.findAll(PageRequest.of(pageInt,pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedBy(admin.getId(), PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
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
                    if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT))
                    {
                        shopsEntity = shopRepository.findByContractTermAfter(startDateFind, PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedByAndContractTermAfter
                                        (admin.getId(),startDateFind,  PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
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
                    if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT))
                    {
                        shopsEntity = shopRepository.findByContractTermBefore(endDateFind, PageRequest.of(pageInt, pagesizeInt));
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository.findByCreatedByAndContractTermBefore
                                        (admin.getId(),endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
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
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
                    {
                        Users admin = userRepository.findByUsername(username);
                        shopsEntity = shopRepository
                                        .findByCreatedByAndContractTermBetween
                                        (admin.getId(),startDateFind,endDateFind, PageRequest.of(pageInt, pagesizeInt) );
                    }
                    else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
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


    public Boolean editShop (HttpServletRequest httpRequest,
                             ReqEditShop requestData,
                             String shopId) throws ParseException {
        try {
            Shops  shop = shopRepository.getOne(shopId);
            if(shop == null || !shop.getOwner().getEnable())
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
                shop.setContractTerm( new SimpleDateFormat("dd/MM/yyyy").parse(requestData.getContractTerm()));
            }
            else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
            {
                //check CreateBY
                if(user.getId().equals(shop.getCreatedBy()))
                {
                    checkAccess = true;
                    shop.setContractTerm( new SimpleDateFormat("dd/MM/yyyy").parse(requestData.getContractTerm()));
                }
            }
            else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
            {

                //check ownerId
                //check CreateBY
                if(user.getId().equals(shop.getOwner().getId()))
                {
                    checkAccess = true;
                }
            }

            //Edit Shop
            if(checkAccess == true)
            {
                Users owner = shop.getOwner();
                owner.setUsername(requestData.getOwnerName());
                owner.setPassword(passwordEncoder.encode(requestData.getOwnerPassword()));
                owner.setEmail(requestData.getOwnerEmail());
                owner.setUpdatedAt(new Date());
                owner.setUpdatedBy(user.getId());
                userRepository.save(owner);

                List<Payments> payments = (List<Payments>) shop.getPayments();

                for (Payments payment : payments)
                {
                    paymentRepository.delete(payment);
                }
                List<Payments> paymentsEdit = new ArrayList<>();
                for(String paymentCode : requestData.getPaymentMethod())
                {
                    Payments payment = paymentRepository.findByCode(paymentCode);
                    paymentsEdit.add(payment);
                }

                shop.setOwner(owner);
                shop.setName(requestData.getShopName());
                shop.setAddress(requestData.getAddress());
                shop.setOpenTime(requestData.getOpenTime());
                shop.setCloseTime(requestData.getCloseTime());
                shop.setWebsite(requestData.getWebsite());
                shop.setWifi(requestData.getWifi());
                shop.setParkingScale(requestData.getParkingScale());
                shop.setSmokingPlace(requestData.getSmokingPlace());
                shop.setPreOrder(requestData.getPreOrder());
                shop.setDescription(requestData.getDescription());
                shop.setCoverImage(requestData.getCoverImage());
                shop.setPayments(paymentsEdit);
                shop.setUpdatedAt(new Date());
                shop.setUpdatedBy(user.getId());
                shopRepository.save(shop);

                //Create log
                logProcess.createLog(new ReqCreateLog(shop, EActionType.UPDATE_DATA),
                        shopRepository,
                        logRepository);
                return true;
            }
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.editShop" + e.getMessage());
        }

        return false;
    }

    public Boolean deleteShop(HttpServletRequest httpRequest,
                              String shopId)
    {
        try {
//          checkShop exist
            Shops  shop = shopRepository.getOne(shopId);
            if(shop == null)
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
                if(user.getId().equals(shop.getCreatedBy()))
                {
                    checkAccess = true;
                }
            }
            else {
                return false;
            }

            if(checkAccess == true)

            {
                shopRepository.delete(shop);

                //Create log
                logProcess.createLog(new ReqCreateLog(shop, EActionType.DELETE),
                        shopRepository,
                        logRepository);
                return true;
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.deleteShop: " + e.getMessage());
            return false;
        }

    }

    public Object getShopById(HttpServletRequest httpRequest,
                               String shopId)
    {
        try {
            //checkShop exist
            Shops  shop = shopRepository.findById(shopId).get();
            if(shop == null || !shop.getOwner().getEnable())
            {
                return null;
            }
            //check Access
            String token = httpRequest.getHeader("Authorization");
            if(token == null)
            {
                ResShopUser response = new ResShopUser(shop);
                return response;
            }
            else
            {
                Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
                if(AttributeTokenService.checkAccess(token,RoleConstant.ROOT)||
                    (AttributeTokenService.checkAccess(token,RoleConstant.ADMIN)
                            &&user.getId().equals(shop.getCreatedBy())) ||
                    (AttributeTokenService.checkAccess(token,RoleConstant.MANAGER)
                            &&user.getId().equals(shop.getOwner().getId())))
                {
                    ResShop response = new ResShop(shop);
                    return response;
                }
                return null;
            }
        } catch (Exception e)
        {
            System.out.println("Err in ShopService.getShopById: " + e.getMessage());
            return null;
        }
    }
}

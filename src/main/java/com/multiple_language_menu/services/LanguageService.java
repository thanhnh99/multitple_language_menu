package com.multiple_language_menu.services;


import com.multiple_language_menu.constants.LanguageConstant;
import com.multiple_language_menu.constants.RoleConstant;
import com.multiple_language_menu.models.entities.Languages;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqAddLanguageForShop;
import com.multiple_language_menu.models.request.ReqUpdateLanguageRank;
import com.multiple_language_menu.models.responses.dataResponse.ResLanguage;
import com.multiple_language_menu.repositories.ILanguageRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {
    @Autowired
    ILanguageRepository languageRepository;
    @Autowired
    IShopRepository shopRepository;
    @Autowired
    IUserRepository userRepository;

    public List<ResLanguage> getLanguagesByShop(String shopId)
    {
        try {
            Shops shop = shopRepository.findById(shopId).get();
            if(shop != null && shop.getOwner().getEnable())
            {
                List<Languages> languagesEntity = languageRepository.findByShopOrderByRank(shop);
                List<ResLanguage> resLanguagesList = new ArrayList<>();
                for (Languages languageEntity : languagesEntity)
                {
                    ResLanguage resLanguage = new ResLanguage(languageEntity);
                    resLanguagesList.add(resLanguage);
                }
                return resLanguagesList;
            }
            return null;
        } catch (Exception e)
        {
            System.out.println("Err in LanguageService.getLanguageByShop: " + e.getMessage());
            return null;
        }
    }

    public Boolean addLanguageForShop(HttpServletRequest httpRequest, ReqAddLanguageForShop reqAddLanguageForShop)
    {
        try {
            Boolean access = false;
            String token = httpRequest.getHeader("Authorization");
            Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            Shops shop = shopRepository.findById(reqAddLanguageForShop.getShopId()).get();
            if(user == null || shop == null)
            {
                access = false;
            }
            else if(
                    AttributeTokenService.checkAccess(token, RoleConstant.ROOT) ||
                        (AttributeTokenService.checkAccess(token, RoleConstant.ADMIN) &&
                                user.getEnable()&&
                                user.getId().equals(shop.getCreatedBy()))
                    )
            {
                access = true;
            }
            if (access)
            {
                if(shop != null)
                {
                    Languages newLanguage = new Languages(reqAddLanguageForShop.getLanguageCode(),
                            reqAddLanguageForShop.getLanguageCode().toUpperCase(),
                            shop.getLanguages().size(),
                            shop);
                    languageRepository.save(newLanguage);
                    return true;
                }
            }
            return false;
        } catch (Exception e)
        {
            System.out.println("Err in LanguageService.getLanguage: " + e.getMessage());
            return false;
        }
    }

    public Boolean updateRankLanguageForShop(HttpServletRequest httpRequest, ReqUpdateLanguageRank reqUpdateLanguageRank)
    {
        Boolean access = false;
        String token = httpRequest.getHeader("Authorization");
        Users user = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
        for (String languageUpdateId : reqUpdateLanguageRank.getLanguageIds()) {
            Languages languageUpdate = languageRepository.findById(languageUpdateId).get();
            if ((AttributeTokenService.checkAccess(token, RoleConstant.ROOT)
                    || (AttributeTokenService.checkAccess(token, RoleConstant.MANAGER) &&
                    user.getId().equals(languageUpdate.getShop().getOwner().getId()))
                    || (AttributeTokenService.checkAccess(token, RoleConstant.ADMIN) &&
                    user.getId().equals(languageUpdate.getShop().getCreatedBy())))
                    &&
                    languageUpdate.getShop().getId().equals(reqUpdateLanguageRank.getShopId())
            ) {
                access = true;
            }
            else {
                access = false;
            }
            if(access == false) break;
        }
        if (access)
        {
            int index = 0;
            for (String itemUpdateId : reqUpdateLanguageRank.getLanguageIds())
            {
                Languages languageUpdate = languageRepository.findById(itemUpdateId).get();
                languageUpdate.setRank(index++);
                languageRepository.save(languageUpdate);
            }
            return true;
        }
        return false;
    }

    public void addDefaultLanguage(String shopId)
    {
        try {
            Shops shop = shopRepository.findById(shopId).get();
            if(shop != null)
            {
                Integer rank = shop.getLanguages().size();
                for (String languageCode: LanguageConstant.languageCode())
                {
                    if(languageRepository.findByCodeAndShop(languageCode, shop) == null)
                    {
                        Languages languageEntity = new Languages(languageCode,
                                                                languageCode,
                                                                rank++,
                                                                shop);
                        languageRepository.save(languageEntity);
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println("Err in LanguageService.addLanguage: " + e.getMessage());
            return;
        }
    }
}

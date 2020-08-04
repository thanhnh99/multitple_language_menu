package com.multiple_language_menu.services;


import com.multiple_language_menu.enums.LanguageConstant;
import com.multiple_language_menu.models.entities.Languages;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.repositories.ILanguageRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    @Autowired
    ILanguageRepository languageRepository;
    @Autowired
    IShopRepository shopRepository;

    public List<Languages> getLanguages()
    {
        try {
            List<Languages> languages = languageRepository.findAll();
            return languages;
        } catch (Exception e)
        {
            System.out.println("Err in LanguageService.getLanguage: " + e.getMessage());
            return null;
        }
    }

    public void addLanguage(String shopId)
    {
        try {
            Shops shop = shopRepository.findById(shopId).get();
            if(shop != null)
            {
                Integer rank = getLanguages().size();
                for (String languageCode: LanguageConstant.languageCode())
                {
                    if(languageRepository.findByCode(languageCode) == null)
                    {
                        Languages languageEntity = new Languages(languageCode,
                                                                languageCode,
                                                                ++rank,
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

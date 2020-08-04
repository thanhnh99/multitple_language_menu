package com.multiple_language_menu.repositories;


import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.ItemsTranslates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IItemTranslateRepository extends JpaRepository<ItemsTranslates, String> {
    ItemsTranslates findByItemAndLanguageCode(Items item , String languageCode);
}

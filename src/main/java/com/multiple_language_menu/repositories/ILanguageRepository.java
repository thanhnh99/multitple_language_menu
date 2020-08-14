package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Languages;
import com.multiple_language_menu.models.entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ILanguageRepository extends JpaRepository<Languages, String> {
    Languages findByCodeAndShop(String code, Shops shop);
    List<Languages> findByShopOrderByRank(Shops shop);
}

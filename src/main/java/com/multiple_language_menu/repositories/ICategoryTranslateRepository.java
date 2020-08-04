package com.multiple_language_menu.repositories;


import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryTranslateRepository extends JpaRepository<CategoriesTranslates, String> {
    void deleteByCategory(Categories categories);

    CategoriesTranslates findByCategoryAndLanguageCode(Categories category, String languageCode);




}

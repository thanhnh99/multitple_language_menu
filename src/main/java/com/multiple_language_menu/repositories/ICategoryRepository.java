package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICategoryRepository extends JpaRepository<Categories, String> {
    List<Categories>  findCategoriesByCategoriesParent(Categories categoryParent);
    List<Categories> findCategoriesByShop(Shops shop);
}

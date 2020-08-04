package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IItemRepository extends JpaRepository<Items, String> {
    List<Items> findByCategory(Categories categories);
}

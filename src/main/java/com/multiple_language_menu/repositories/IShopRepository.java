package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IShopRepository  extends JpaRepository<Shops, UUID> {
}

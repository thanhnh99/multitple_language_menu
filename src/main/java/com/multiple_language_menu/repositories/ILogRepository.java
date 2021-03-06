package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Logs;
import com.multiple_language_menu.models.entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ILogRepository extends JpaRepository<Logs, String> {
    List<Logs> findAllByOrderByCreatedAtDesc();
    List<Logs> findAllByShopOrderByCreatedAtDesc(Shops shop);
}

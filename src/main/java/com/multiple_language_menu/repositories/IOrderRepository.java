package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, String> {

}

package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetails, String> {

}

package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPaymentRepository extends JpaRepository<Payments, String> {
    Payments findByCode(String code);
}

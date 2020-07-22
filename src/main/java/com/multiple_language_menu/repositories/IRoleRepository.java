package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<Roles, UUID> {
    Roles findByCode(String code);
}

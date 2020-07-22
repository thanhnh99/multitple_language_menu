package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
    Users findByUsername(String username);
}

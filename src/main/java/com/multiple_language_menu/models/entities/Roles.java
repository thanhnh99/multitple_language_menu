package com.multiple_language_menu.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles extends BaseEntity{
    @Id
    private UUID id = UUID.randomUUID();
    private String name;
    private String code;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;
}

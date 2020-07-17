package com.multiple_language_menu.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity{
    @Id
    private UUID id = UUID.randomUUID();
    private String username;
    private String password;
    private String email;
    private Boolean enable = true;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Roles> roles;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Shops> shops;
}

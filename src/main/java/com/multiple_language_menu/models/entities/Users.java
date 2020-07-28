package com.multiple_language_menu.models.entities;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity{

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private Boolean enable = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Collection<Roles> roles = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Shops> shops;

    public Users(String username, String password, String email, Boolean enable)
    {
        this.roles = new ArrayList<Roles>();
        this.username = username;
        this.password = password;
        this.email = email;
        this.enable = enable;
    }
    public void addRole(Roles role)
    {
        this.roles.add(role);
    }

}

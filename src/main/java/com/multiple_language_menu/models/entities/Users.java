package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.models.auth.GranAuthorityImpl;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

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

    @ManyToMany(fetch = FetchType.EAGER)
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

    public List<GranAuthorityImpl> getRolesAuthority(){
        return this.roles.stream().map(role -> {
            return new GranAuthorityImpl(role.getName());
        }).collect(Collectors.toList());
    }

    public List<String> getRoles()
    {
        List<Roles> roles = (List<Roles>) this.roles;
        List<String> listRole = new ArrayList<>();
        for (Roles role : roles)
        {
            listRole.add(role.getCode());
        }
        return listRole;
    }
}

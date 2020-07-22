package com.multiple_language_menu.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles extends BaseEntity{

    private String name;
    @Column(unique = true)
    private String code;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @ToString.Exclude
    private Collection<Users> users = new ArrayList<Users>();

    public Roles(String name, String code)
    {
        this.name = name;
        this.code = code;
        this.users = new ArrayList<Users>();
    }

    public void addUser(Users user)
    {
        this.users.add(user);
    }
}

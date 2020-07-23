package com.multiple_language_menu.configs;

import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.repositories.IRoleRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class InitData {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    public InitData(IRoleRepository roleRepository, IUserRepository userRepository)
    {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public void addRole()
    {
        if (roleRepository.findByCode("root") == null)
        {
            try {
                Roles role = new Roles("ROOT","root");
                roleRepository.save(role);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }

        }
        if (roleRepository.findByCode("admin") == null)
        {
            try {
                Roles role = new Roles("ADMIN","admin");
                roleRepository.save(role);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }

        }
    }
    @Bean
    public void addAdmin()
    {
        if(userRepository.findByEmail("admin@gmail.com") == null)
        {
            try {
                Roles role = roleRepository.findByCode("admin");
                Users rootUser = new Users("adminname","adminpass","admin@gmail.com", new Boolean(true));
                rootUser.addRole(role);
                userRepository.save(rootUser);
//                role.addUser(rootUser);
//                roleRepository.save(role);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }

        }
    }


    @Bean
    public void addRoot()
    {
        if(userRepository.findByEmail("root@gmail.com") == null)
        {
            try {
                Roles role = roleRepository.findByCode("root");
                Users rootUser = new Users("rootname","rootpass","root@gmail.com", new Boolean(true));
                rootUser.addRole(role);
                userRepository.save(rootUser);
//                role.addUser(rootUser);
//                roleRepository.save(role);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }

        }
    }
}

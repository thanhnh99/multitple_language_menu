package com.multiple_language_menu.configs;

import com.multiple_language_menu.models.entities.*;
import com.multiple_language_menu.repositories.ILanguageRepository;
import com.multiple_language_menu.repositories.IPaymentRepository;
import com.multiple_language_menu.repositories.IRoleRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class InitData {
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IPaymentRepository paymentRepository;
    private final ILanguageRepository languageRepository;

    public InitData(PasswordEncoder passwordEncoder, IRoleRepository roleRepository, IUserRepository userRepository, IPaymentRepository paymentRepository, ILanguageRepository languageRepository)
    {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.languageRepository = languageRepository;
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
        if (roleRepository.findByCode("manager") == null)
        {
            try {
                Roles role = new Roles("MANAGER","manager");
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
                Users rootUser = new Users("adminname",passwordEncoder.encode("adminpass"),"admin@gmail.com", new Boolean(true));
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
                Users rootUser = new Users("rootname",passwordEncoder.encode("rootpass"),"root@gmail.com", new Boolean(true));
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
    public void addPaymentMethod()
    {
        if(paymentRepository.findByCode("by_cash") == null)
        {
            try {
                Payments cashPay = new Payments("Thanh toan tien mat", "by_cash", new ArrayList<Shops>());
                paymentRepository.save(cashPay);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }
        }

        if(paymentRepository.findByCode("by_card") == null)
        {
            try {
                Payments cashPay = new Payments("Thanh toan the", "by_card", new ArrayList<Shops>());
                paymentRepository.save(cashPay);
            } catch (Exception e)
            {
                System.out.println("ERR: " + e.getMessage());
            }
        }
    }


    @Bean
    public void addShop()
    {

    }

}

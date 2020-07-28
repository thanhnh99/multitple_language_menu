package com.multiple_language_menu.services;

import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.repositories.IRoleRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    public Boolean checkUserExisted(String username)
    {
        try {
            Users user = userRepository.findByUsername(username);
            if(user == null)
            {
                return false;
            }
            return true;
        }catch (NullPointerException e)
        {
            return true;
        }
    }

    public Users createManager(HttpServletRequest httpRequest, ReqCreateAdmin requestData)
    {
        String token = httpRequest.getHeader("Authorization");
        try {
            String userRequestName = AttributeTokenService.getUsernameFromToken(token);
            Users requestUser = userRepository.findByUsername(userRequestName);
            boolean check = AttributeTokenService.checkAccess(token,"admin");
            boolean checkuserexist = this.checkUserExisted(requestData.getUserName());
            if(AttributeTokenService.checkAccess(token,"admin") &&
                    !this.checkUserExisted(requestData.getUserName()))
            {
                Roles role = roleRepository.findByCode("manager");
                Users manager = new Users(requestData.getUserName(),
                        requestData.getPassword(),
                        requestData.getEmail(),
                        true);
                manager.setCreated_by(requestUser.getId());
                manager.addRole(role);
                userRepository.save(manager);
                return manager;
            }
            else
            {
                return null;
            }
        }catch (Exception e)
        {
            System.out.println("Err in UserService.createManager: "+ e.getMessage());
            return null;
        }
    }

    public Users createAdmin(HttpServletRequest httpRequest, ReqCreateAdmin requestData)
    {
        String token = httpRequest.getHeader("Authorization");
        try {
            String userRequestName = AttributeTokenService.getUsernameFromToken(token);
            Users requestUser = userRepository.findByUsername(userRequestName);
            if(AttributeTokenService.checkAccess(token,"root")&&
                    userRepository.findByUsername(requestData.getUserName()) == null)
            {
                Roles role = roleRepository.findByCode("admin");
                Users admin = new Users(requestData.getUserName(),
                        requestData.getPassword(),
                        requestData.getEmail(),
                        true);
                admin.setCreated_by(requestUser.getId());
                admin.addRole(role);
                userRepository.save(admin);
                return admin;
            }
            else
            {
                return null;
            }
        }catch (Exception e)
        {
            System.out.println("Err in UserService.createManager: "+ e.getMessage());
            return null;
        }
    }
}

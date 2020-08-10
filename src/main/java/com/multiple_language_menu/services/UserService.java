package com.multiple_language_menu.services;

import com.multiple_language_menu.filters.JwtTokenProvider;
import com.multiple_language_menu.models.auth.CustomUserDetail;
import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.models.responses.dataResponse.ResLogin;
import com.multiple_language_menu.repositories.IRoleRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResLogin authenticateUser(ReqLogin request)
    {
        ResLogin response = new ResLogin();
        CustomUserDetail customUserDetail = loadUserByUserName(request.getUsername());
        if(customUserDetail == null ||
                !passwordEncoder.matches(request.getPassword(), customUserDetail.getPassword()))
        {
            response = null;
        }
        else
        {
            if(customUserDetail.getUser().getEnable())
            {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

                String token = jwtTokenProvider.generateJwt(customUserDetail);
                response.setToken(token);
                response.setRoles(customUserDetail.getAuthorities());
            }
            else
            {
                response = null;
            }
        }
        return response;
    }

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
            boolean checkPermission = AttributeTokenService.checkAccess(token,"admin") || AttributeTokenService.checkAccess(token,"root");
            boolean checkuserexist = this.checkUserExisted(requestData.getUserName());
            if(checkPermission && !checkuserexist)
            {
                Roles role = roleRepository.findByCode("manager");
                Users manager = new Users(requestData.getUserName(),
                        passwordEncoder.encode(requestData.getPassword()),
                        requestData.getEmail(),
                        true);
                manager.setCreatedBy(requestUser.getId());
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
                admin.setCreatedBy(requestUser.getId());
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

    public Boolean loginEnable(HttpServletRequest httpRequest, String userId)
    {
        String token = httpRequest.getHeader("Authorization");
        try{

            Users loginEnableUser = userRepository.getOne(userId);
            Users userRequest = userRepository.findByUsername(AttributeTokenService.getUsernameFromToken(token));
            boolean checkAcess = false;

            if(AttributeTokenService.checkAccess(token, "root"))
            {
                checkAcess = true;
            }
            else if(AttributeTokenService.checkAccess(token, "admin"))
            {
                if(loginEnableUser.getCreatedBy().equals(userRequest.getId()));
                {
                    checkAcess = true;
                }
            }

            if(checkAcess == true)
            {
                loginEnableUser.setUpdatedBy(userRequest.getId());
                loginEnableUser.setEnable(!loginEnableUser.getEnable());
                loginEnableUser.setUpdatedAt(new Date());
                userRepository.save(loginEnableUser);
            }
            return checkAcess;
        } catch (Exception e)
        {
            System.out.println("Err in UserService.loginEnable");
            return false;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Kiểm tra user có tồn tại không
        Users user = userRepository.findByUsername(username);
        if(user == null)
        {
            throw new UsernameNotFoundException(username);
        }
        return  new CustomUserDetail(user);
    }

    public CustomUserDetail loadUserByUserName(String userName){
        Users user = userRepository.findByUsername(userName);
        if(user == null) {
            return null;
        }
        return new CustomUserDetail(user);
    }


}

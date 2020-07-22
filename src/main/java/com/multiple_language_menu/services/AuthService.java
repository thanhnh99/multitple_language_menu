package com.multiple_language_menu.services;

import com.multiple_language_menu.filters.TokenJwtUtil;
import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.models.responses.dataResponse.ResLogin;
import com.multiple_language_menu.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class AuthService {
    @Autowired
    IUserRepository userRepository;
    public ResLogin login(ReqLogin reqLogin)
    {
        try {
            Users user = userRepository.findByUsername(reqLogin.getUsername());
            if(user.getEnable()&&user.getPassword().equals(reqLogin.getPassword()))
            {
                ArrayList<String> roles = new ArrayList<>();
                for(Roles role : user.getRoles())
                {
                    roles.add(role.getName());
                }
                String token = TokenJwtUtil.generateJwt(reqLogin);
                ResLogin response = new ResLogin();
                response.setRole(roles);
                response.setToken(token);
                return response;
            }
            return null;
        }catch (Exception e)
        {
            System.out.println("Err: " + e.getMessage());
            return null;
        }
    }
}

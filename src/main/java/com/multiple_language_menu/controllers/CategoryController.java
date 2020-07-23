package com.multiple_language_menu.controllers;

import com.multiple_language_menu.services.AppAuthorizerImpl;
import com.multiple_language_menu.services.AttributeTokenService;
import com.multiple_language_menu.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    TokenAuthenticationService authenticationService;

    @GetMapping()
//    @RolesAllowed({ "ROOT" })
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'ROOT.ADMIN',this)")
    public String getAll(HttpServletRequest request)
    {
        String token = request.getHeader("Authorization");
        AttributeTokenService.getRolesFromToken(token);

        return  "ALL CATEGORY";
//        authenticationService.getRolesFromToken(request);
//        return authenticationService.getRolesFromToken(request);
    }
}

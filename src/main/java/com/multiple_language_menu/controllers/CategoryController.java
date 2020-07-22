package com.multiple_language_menu.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping()
    @RolesAllowed({ "ADMIN", "ROOT" })
//    @PreAuthorize("@appAuthorizer.authorize(authentication, {ROOT, ADMIN} , this)")
    public String getAll()
    {
        return "All category";
    }
}

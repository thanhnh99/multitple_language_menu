package com.multiple_language_menu.controllers;

import com.multiple_language_menu.services.authorize.AttributeTokenService;
import com.multiple_language_menu.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping()
//    @RolesAllowed({ "ROLE_ROOT" })
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin'})")
    public String getCategories(HttpServletRequest request,
                                @RequestParam(required = true) String page,
                                @RequestParam(required = true) String pagesize,
                                @RequestParam(required = false) String shopId)
    {
        String token = request.getHeader("Authorization");
        if(AttributeTokenService.checkAccess(token,"ROOT"))
        {
            //Service getCategoryRoot
        }else if (AttributeTokenService.checkAccess(token,"ADMIN"))
        {
            //Service getCategoryAdmin
        }

        return  "ALL CATEGORY";
    }
}

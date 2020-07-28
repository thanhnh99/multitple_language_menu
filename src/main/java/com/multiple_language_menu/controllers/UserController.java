package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.responses.dataResponse.ResCreateManager;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root'})")
    public ResponseEntity addAdmin(HttpServletRequest request,
                                   @RequestBody ReqCreateAdmin requestData)
    {
        HttpResponse response = new HttpResponse();
        if(userService.createAdmin(request,requestData) != null)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.ok(response);
    }
}

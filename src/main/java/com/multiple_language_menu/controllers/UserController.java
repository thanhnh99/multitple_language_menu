package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateAdmin;
import com.multiple_language_menu.models.responses.dataResponse.ResCreateManager;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/manager")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'admin'})")
    public ResponseEntity addManager(HttpServletRequest request,
                                   @RequestBody ReqCreateAdmin requestData)
    {
        HttpResponse response = new HttpResponse();
        if(userService.createManager(request,requestData) != null)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.ok(response);
    }


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

    @PutMapping("{userId}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root','admin'})")
    public ResponseEntity<HttpResponse> loginEnable(HttpServletRequest httpRequest,
                                                    @PathVariable String userId)
    {
        HttpResponse response = new HttpResponse();
        if(userService.loginEnable(httpRequest,userId))
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

package com.multiple_language_menu.controllers;

import com.multiple_language_menu.filters.TokenJwtUtil;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.models.responses.dataResponse.ResLogin;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody ReqLogin request)
    {
        HttpResponse<ResLogin> response = new HttpResponse();
        if (authService.login(request) != null)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            response.addData(authService.login(request));
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("Login fail");
        response.setData(null);
        return ResponseEntity.status(400).body(response);
    }
}

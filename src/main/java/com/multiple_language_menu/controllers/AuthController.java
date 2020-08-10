package com.multiple_language_menu.controllers;

import com.multiple_language_menu.filters.JwtTokenProvider;
import com.multiple_language_menu.models.auth.CustomUserDetail;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.models.responses.dataResponse.ResLogin;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody ReqLogin request)
    {
        HttpResponse response = new HttpResponse();
        if(userService.authenticateUser(request) != null)
        {
            response.setMessage("success");
            response.setStatusCode("200");
            response.setData(userService.authenticateUser(request));
            return ResponseEntity.status(200).body(response);
        }
        response.setMessage("bad request");
        response.setStatusCode("400");
        response.setData(userService.authenticateUser(request));
        return ResponseEntity.status(400).body(response);
    }
}

package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.responses.dataResponse.ResLog;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    LogService logService;

    @GetMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse<List<ResLog>>> getLogs(HttpServletRequest request)
    {
        HttpResponse<List<ResLog>> httpResponse = new HttpResponse<>();
        List<ResLog> responseData = logService.getLogs(request);
        httpResponse.setStatusCode("200");
        httpResponse.setMessage("success");
        httpResponse.setData(responseData);
        return ResponseEntity.status(200).body(httpResponse);
    }
}

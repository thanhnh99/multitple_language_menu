package com.multiple_language_menu.controllers;


import com.multiple_language_menu.models.request.ReqCreateOrder;
import com.multiple_language_menu.models.responses.dataResponse.ResCategory;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("log")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> createOrder(@RequestBody ReqCreateOrder requestData)
    {
        HttpResponse httpResponse = new HttpResponse<>();
        boolean responseData =  orderService.createOrder(requestData);
        if(responseData)
        {
            httpResponse.setStatusCode("200");
            httpResponse.setMessage("success");
            httpResponse.setData(responseData);
            return new ResponseEntity(responseData, HttpStatus.OK);

        }
        httpResponse.setStatusCode("400");
        httpResponse.setMessage("bad request");
        httpResponse.setData(responseData);
        return ResponseEntity.status(400).body(httpResponse);
    }

}

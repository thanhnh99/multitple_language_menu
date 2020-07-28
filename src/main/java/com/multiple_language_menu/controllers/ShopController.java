package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.models.responses.dataResponse.ResShop;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @PostMapping()
    public ResponseEntity<HttpResponse> addShop(HttpServletRequest httpRequest, @RequestBody ReqCreateShop requestData)
    {
        HttpResponse<ResShop> response = new HttpResponse();
        Boolean responseData = shopService.createShop(httpRequest, requestData);
        if(responseData)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            response.setData(null);
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        response.setData(null);
        return ResponseEntity.status(400).body(response);
    }

    @GetMapping()
    public ResponseEntity<HttpResponse> getShops(HttpServletRequest httpRequest,
                                                 @RequestParam(required = false) String page,
                                                 @RequestParam(required = false) String pagesize,
                                                 @RequestParam(required = false) String startDate,
                                                 @RequestParam(required = false) String endDate)
    {
        HttpResponse<List<ResShop>> response = new HttpResponse();
        List<ResShop> responseData = shopService.getShops(httpRequest);
        if(responseData != null)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            response.setData(responseData);
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        response.setData(null);
        return ResponseEntity.status(400).body(response);
    }


}

package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateShop;
import com.multiple_language_menu.models.request.ReqEditShop;
import com.multiple_language_menu.models.responses.dataResponse.ResShop;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @PostMapping()
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin'})")
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

    @PostMapping("{shopId}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'manager'})")
    public ResponseEntity<HttpResponse> uploadCoverImage(HttpServletRequest httpRequest,
                                                         @PathVariable(name = "shopId") String shopId,
                                                         @RequestParam("coverImage") MultipartFile coverImage)
    {
        HttpResponse response = new HttpResponse();
        Boolean responseData = shopService.uploadCoverImage(httpRequest,shopId, coverImage);
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
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> getShops(HttpServletRequest httpRequest,
                                                 @RequestParam(required = true) String page,
                                                 @RequestParam(required = true) String pagesize,
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

    @PutMapping("{shopId}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> editShop(HttpServletRequest httpRequest,
                                                 @RequestBody ReqEditShop requestData,
                                                 @PathVariable String shopId) throws ParseException
    {
        HttpResponse<List<ResShop>> response = new HttpResponse();
        if(shopService.editShop(httpRequest, requestData,shopId))
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

    @DeleteMapping("{shopId}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> deleteShop(HttpServletRequest httpRequest,
                                                   @PathVariable String shopId)
    {
        HttpResponse<List<ResShop>> response = new HttpResponse();
        if(shopService.deleteShop(httpRequest ,shopId))
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(400).body(response);
    }

    @GetMapping("{shopId}")
    public ResponseEntity<HttpResponse> getShopById(HttpServletRequest httpRequest,
                                                   @PathVariable String shopId)
    {
        HttpResponse<Object> response = new HttpResponse();
        Object responseData = shopService.getShopById(httpRequest ,shopId);
        if(responseData != null)
        {
            response.setStatusCode("200");
            response.setMessage("success");
            response.setData(responseData);
            return ResponseEntity.ok(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(400).body(response);
    }

}

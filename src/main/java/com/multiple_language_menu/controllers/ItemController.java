package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateItem;
import com.multiple_language_menu.models.request.ReqEditItem;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'manager'})")
    public ResponseEntity<HttpResponse> createItem(HttpServletRequest httpServletRequest,
                                                 @PathVariable String itemId,
                                                 @RequestBody ReqCreateItem requestData)
    {
        HttpResponse response = new HttpResponse();
        if (itemService.createItem(httpServletRequest,
                requestData))
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.status(200).body(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{item}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'manager'})")
    public ResponseEntity<HttpResponse> editItem(HttpServletRequest httpServletRequest,
                                                 @PathVariable String itemId,
                                                 @RequestBody ReqEditItem requestData)
    {
        HttpResponse response = new HttpResponse();
        if (itemService.editItem(httpServletRequest,
                requestData))
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.status(200).body(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'manager'})")
    public ResponseEntity<HttpResponse> deleteItem(HttpServletRequest httpServletRequest,
                                                   @PathVariable String itemId)
    {
        HttpResponse response = new HttpResponse();
        if (itemService.deleteItem(httpServletRequest,
                itemId))
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.status(200).body(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(200).body(response);
    }


}

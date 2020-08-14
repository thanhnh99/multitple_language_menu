package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqAddLanguageForShop;
import com.multiple_language_menu.models.request.ReqUpdateLanguageRank;
import com.multiple_language_menu.models.responses.dataResponse.ResLanguage;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.LanguageService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("language")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @GetMapping
    public ResponseEntity<HttpResponse<List<ResLanguage>>> getLanguageByShop (@RequestParam(name = "shopId") String shopId)
    {
        HttpResponse response = new HttpResponse();
        List<ResLanguage> responseData = languageService.getLanguagesByShop(shopId);
        if(responseData == null)
        {
            response.setStatusCode("400");
            response.setMessage("bad request");
            response.setData(null);
            return ResponseEntity.status(400).body(response);
        }
        else
        {
            response.setData(responseData);
            response.setStatusCode("success");
            response.setStatusCode("200");
            return ResponseEntity.status(200).body(response);
        }
    }

    @PutMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> updateRank(HttpServletRequest httpRequest,
                                                   @RequestBody ReqUpdateLanguageRank reqUpdateLanguageRank)
    {
        HttpResponse response = new HttpResponse();
        if(languageService.updateRankLanguageForShop(httpRequest,reqUpdateLanguageRank))
        {
            response.setData(true);
            response.setMessage("success");
            response.setStatusCode("200");
            return ResponseEntity.status(200).body(response);
        }
        else
        {
            response.setStatusCode("400");
            response.setMessage("bad request");
            response.setData(false);
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<HttpResponse> addLanguageForShop(HttpServletRequest httpRequest,
                                                           @RequestBody ReqAddLanguageForShop reqAddLanguageForShop)
    {
        HttpResponse response = new HttpResponse();
        if(languageService.addLanguageForShop(httpRequest, reqAddLanguageForShop))
        {
            response.setData(true);
            response.setStatusCode("success");
            response.setStatusCode("200");
            return ResponseEntity.status(200).body(response);
        }
        else
        {
            response.setStatusCode("400");
            response.setMessage("bad request");
            response.setData(false);
            return ResponseEntity.status(400).body(response);
        }
    }
}

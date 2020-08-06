package com.multiple_language_menu.controllers;

import com.multiple_language_menu.models.request.ReqCreateCategory;
import com.multiple_language_menu.models.request.ReqEditCategory;
import com.multiple_language_menu.models.request.ReqEditItem;
import com.multiple_language_menu.models.responses.dataResponse.ResCategory;
import com.multiple_language_menu.models.responses.dataResponse.ResItem;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import com.multiple_language_menu.services.CategoryService;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping()
//    @RolesAllowed({ "ROLE_ROOT" })
//    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin'})")
    public ResponseEntity<?> getCategories(HttpServletRequest request)
//                                @RequestParam(required = true) String page,
//                                @RequestParam(required = true) String pagesize,
//                                @RequestParam(required = false) String shopId)
    {
        HttpResponse<List<ResCategory>> httpResponse = new HttpResponse<>();
        List<ResCategory> responseData = (List) categoryService.getCategoryByShopId(request);
        if(responseData != null)
        {
            httpResponse.setStatusCode("200");
            httpResponse.setMessage("success");
            httpResponse.setData(responseData);
            return new ResponseEntity(responseData, HttpStatus.OK);
//            return ResponseEntity.status(200).body(httpResponse);

        }
        httpResponse.setStatusCode("400");
        httpResponse.setMessage("bad request");
        httpResponse.setData(null);
        return ResponseEntity.status(400).body(httpResponse);
    }

    @GetMapping("/{categoryId}/child")
    public ResponseEntity<HttpResponse<List<Object>>> getChildFromCategory(
            @PathVariable String categoryId,
            @RequestParam("language_code") String languageCode
    )
    {
        HttpResponse<List<Object>> httpResponse = new HttpResponse<>();
        List<Object> responseData = categoryService.getChildByCategoryId(categoryId,languageCode);
        if(responseData != null)
        {
            httpResponse.setStatusCode("200");
            httpResponse.setMessage("success");
            httpResponse.setData(responseData);
            return ResponseEntity.status(200).body(httpResponse);

        }
        httpResponse.setStatusCode("400");
        httpResponse.setMessage("bad request");
        httpResponse.setData(null);
        return ResponseEntity.status(400).body(httpResponse);
    }

    @PutMapping
    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'manager'})")
    public ResponseEntity<HttpResponse> editCategory(HttpServletRequest httpRequest,
                                               @RequestBody ReqEditCategory requestData)
    {
        HttpResponse response = new HttpResponse();
        if (categoryService.editCategory(httpRequest, requestData))
        {
            response.setStatusCode("200");
            response.setMessage("success");
            return ResponseEntity.status(200).body(response);
        }
        response.setStatusCode("400");
        response.setMessage("bad request");
        return ResponseEntity.status(400).body(response);
    }


    @PostMapping
//    @PreAuthorize("@appAuthorizer.authorize(authentication, {'root', 'admin', 'manager'})")
    public ResponseEntity<HttpResponse> createCategory(HttpServletRequest httpRequest,
                                                       @RequestBody ReqCreateCategory requestData)
    {
        HttpResponse response = new HttpResponse();
        if (categoryService.createCategory(httpRequest, requestData))
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

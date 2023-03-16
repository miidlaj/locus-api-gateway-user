package com.midlaj.apiGatewayUser.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "resort-service",
        path = "/api/resort/category/",
        configuration = FeignConfiguration.class
)
public interface ResortCategoryRequest {

    @PostMapping
    ResponseEntity<?> addResortCategory(@RequestBody Object requestBody);


    @GetMapping
    ResponseEntity<?> resortCategoryList();
}

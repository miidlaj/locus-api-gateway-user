package com.midlaj.apiGatewayUser.request.resort;

import com.midlaj.apiGatewayUser.request.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "resort-service", //Name of course-service application
        path = "/api/resort/",//Pre-path for course service
        //url = "${resort.service.url}",
        configuration = FeignConfiguration.class
)
public interface ResortServiceRequest {

    @PostMapping
    ResponseEntity<?> saveResort(@RequestBody Object requestBody);
}

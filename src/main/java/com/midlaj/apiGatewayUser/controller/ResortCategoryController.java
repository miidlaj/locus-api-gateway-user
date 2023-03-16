//package com.midlaj.apiGatewayUser.controller;
//
//import com.midlaj.apiGatewayUser.request.ResortCategoryRequest;
//import feign.FeignException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@RestController
//@RequestMapping("/api/resort/category")
//@Slf4j
//public class ResortCategoryController {
//
//    @Autowired
//    private ResortCategoryRequest resortCategoryRequest;
//
//    @GetMapping()
//    public ResponseEntity<?> resortCategoryList() {
//        log.info("Inside the resortCategoryList method");
//
//        ResponseEntity response;
//        try {
//            response = resortCategoryRequest.resortCategoryList();
//            return response;
//        } catch (FeignException e) {
//            HttpStatus status = HttpStatus.valueOf(e.status());
//            return ResponseEntity.status(status).body(e.getMessage());
//        }
//    }
//
//    @PostMapping()
//    public ResponseEntity<?> addResortCategory(@RequestBody Object categoryObject) {
//        log.info("Inside the addResortCategory method");
//
//        ResponseEntity response;
//        try {
//            response = resortCategoryRequest.addResortCategory(categoryObject);
//            return response;
//        } catch (FeignException e) {
//            HttpStatus status = HttpStatus.valueOf(e.status());
////            HttpHeaders headers = new HttpHeaders();
////            for (Map.Entry<String, Collection<String>> entry : e.responseHeaders().entrySet()) {
////                headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
////            }
//            String responseBody = e.responseBody()
//                    .map(buffer -> {
//                        byte[] bytes = new byte[buffer.remaining()];
//                        buffer.get(bytes);
//                        return new String(bytes, StandardCharsets.UTF_8);
//                    })
//                    .orElse("");
//            if (responseBody.matches("[A-Za-z0-9+/]*={0,2}")) {
//                byte[] responseBodyBytes = Base64.getDecoder().decode(responseBody);
//                responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
//            }
//            return ResponseEntity.status(status).body(responseBody);
//        }
//    }
//}

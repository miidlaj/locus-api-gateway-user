//package com.midlaj.apiGatewayUser.controller;
//
//import com.midlaj.apiGatewayUser.utils.ControllerHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/resort/facility")
//@Slf4j
//public class ResortFacilityController {
//
//
//    @GetMapping()
//    public ResponseEntity<?> resortFacilityList() {
//        ResponseEntity response = ControllerHelper.sendAuthenticatedRequest(HttpMethod.GET, "http://localhost:9001/api/resort/facility", null);
//        return response;
//    }
//
//    @PostMapping()
//    public ResponseEntity<?> addResortFacility(@RequestBody Object facilityRequestDTO) {
//        ResponseEntity response = ControllerHelper.sendAuthenticatedRequest(HttpMethod.POST, "http://localhost:9001/api/resort/facility", facilityRequestDTO);
//        return response;
//    }
//}

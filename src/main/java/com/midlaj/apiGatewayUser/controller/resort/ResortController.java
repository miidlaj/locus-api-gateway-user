//package com.midlaj.apiGatewayUser.controller.resort;
//
//import com.midlaj.apiGatewayUser.request.resort.ResortServiceRequest;
//import com.midlaj.resort.entity.Resort;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/resorts")
//@Slf4j
//public class ResortController {
//
//    @Autowired
//    private ResortServiceRequest resortServiceRequest;
//
//
//    @PostMapping()
//    public ResponseEntity<?> saveResort(@Valid @RequestBody Object resortRequestDTO) {
//        log.info("Inside saveResort method of Resort controller");
//        return resortServiceRequest.saveResort(resortRequestDTO);
//    }
//
//    @GetMapping()
//    public List<Resort> getAllResort() {
//        log.info("Inside getAllResort method of Resort controller");
//        return resortService.findAll();
//    }
//}

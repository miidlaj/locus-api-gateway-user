//package com.midlaj.apiGatewayUser.utils;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//
//public class ControllerHelper {
//
//    public static String extractErrorMessage(String errorMessage) {
//        String[] parts = errorMessage.split(":");
//        return parts[parts.length - 1].trim();
//    }
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${service.security-secure-key-username}")
//    private static String USERNAME;
//
//    @Value("${service.security-secure-key-password}")
//    private static String PASSWORD;
//
//    public static ResponseEntity<?> sendAuthenticatedRequest(HttpMethod method, String url, Object requestBody) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Create HttpHeaders object with any necessary headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json"); // Example: setting JSON content type
//
//        // Create HttpEntity with headers and request body
//        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
//
//        // Send request with RestTemplate and handle error response
//        try {
//            ResponseEntity<?> response = restTemplate.exchange(url, method, entity, Object.class);
//            return response;
//        } catch (HttpClientErrorException ex) {
//            // Handle client-side error (4xx)
//            String errorResponse = ex.getResponseBodyAsString();
//            HttpStatusCode statusCode = ex.getStatusCode();
//            // Do something with the error response and status code
//            return new ResponseEntity<Object>(errorResponse, statusCode);
//        } catch (HttpServerErrorException ex) {
//            // Handle server-side error (5xx)
//            String errorResponse = ex.getResponseBodyAsString();
//            HttpStatusCode statusCode = ex.getStatusCode();
//            // Do something with the error response and status code
//            return new ResponseEntity<Object>(errorResponse, statusCode);
//        }
//    }
//}

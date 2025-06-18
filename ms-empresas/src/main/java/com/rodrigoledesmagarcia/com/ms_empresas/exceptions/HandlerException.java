package com.rodrigoledesmagarcia.com.ms_empresas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class HandlerException {

    public static ResponseEntity<Map<String, Object>> errorResponse (HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());
        return ResponseEntity.status(status).body(response);
    }

    // Http 500 Internal Server Error
    public static ResponseEntity<Map<String, Object>> internalServerError (String message){
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Http 404 not found
    public static ResponseEntity<Map<String, Object>> notFound (String message) {
        return errorResponse(HttpStatus.NOT_FOUND, message);
    }

    // Http 400 bad request
    public static ResponseEntity<Map<String, Object>> badRequest (String message) {
        return errorResponse(HttpStatus.BAD_REQUEST, message);
    }
}

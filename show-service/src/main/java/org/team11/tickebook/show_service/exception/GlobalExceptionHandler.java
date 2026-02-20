package org.team11.tickebook.show_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team11.tickebook.show_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("SEAT_STATE_ERROR", ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(Exception ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("GENERAL_ERROR", ex.getMessage()));
    }
}

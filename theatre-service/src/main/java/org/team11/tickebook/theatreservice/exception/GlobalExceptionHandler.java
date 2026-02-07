package org.team11.tickebook.theatreservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicateKey(DataIntegrityViolationException ex) {
        System.out.println(ex.getMessage());
        String message = "Duplicate entry";

        // Optional: detect specific constraint
        if (ex.getMessage() != null &&
                ex.getMessage().contains("uk_theatre_screen_number")) {
            message = "Screen number already exists for this theatre";
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> anyExceptionHandler(Exception ex){
        System.out.println(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }
}


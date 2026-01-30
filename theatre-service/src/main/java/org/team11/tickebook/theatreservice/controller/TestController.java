package org.team11.tickebook.theatreservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/theatre")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().body("Theatre service is accesible");
    }
}

package org.team11.tickebook.consumerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/browse")
public class BrowseController {
    @GetMapping
    public ResponseEntity<?> getShows(){
        return null;
    }
}

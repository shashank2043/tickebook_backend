package org.team11.tickebook.consumerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.consumerservice.cilent.ShowClient;

@RestController
@RequestMapping("/api/browse")
public class BrowseController {
    @Autowired
    private ShowClient showClient;

    @GetMapping
    public ResponseEntity<?> getShows() {
        return ResponseEntity.ok(showClient.getAllShows());
    }
}

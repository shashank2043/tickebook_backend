package org.team11.tickebook.theatreservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.theatreservice.dto.request.TheatreOwnerProfileRequest;
import org.team11.tickebook.theatreservice.service.TheatreOwnerProfileService;


@RestController
@RequestMapping("/theatre")
@RequiredArgsConstructor
public class TheatreOwnerController {
    private final TheatreOwnerProfileService profileService;
    @PostMapping("/owner/profile")
    public ResponseEntity<?> createTheatreOwnerProfile(@RequestBody @Valid TheatreOwnerProfileRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.registerTheatreOwnerProfile(request));
    }
}

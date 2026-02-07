package org.team11.tickebook.theatreservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/internal/owner")
@RequiredArgsConstructor
public class InternalController {

    private final OwnerProfileService ownerProfileService;

    @PutMapping("/{ownerProfileId}/verify")
    public ResponseEntity<String> verifyOwner(
            @PathVariable UUID ownerProfileId
    ) {
        ownerProfileService.verifyOwner(ownerProfileId);
        return ResponseEntity.ok("Owner profile verified");
    }
}

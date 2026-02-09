package org.team11.tickebook.theatreservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.dto.response.SeatDto;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final OwnerProfileService ownerProfileService;
    private final SeatService seatService;

    @PutMapping("/owner/{ownerProfileId}/verify")
    public ResponseEntity<String> verifyOwner(
            @PathVariable UUID ownerProfileId
    ) {
        ownerProfileService.verifyOwner(ownerProfileId);
        return ResponseEntity.ok("Owner profile verified");
    }
    @GetMapping("/seats/screen/{screenId}")
    public List<SeatDto> getByScreen(
            @PathVariable Long screenId,
            Authentication authentication
    ) {
        return seatService.getByScreenAsDto(screenId, authentication);
    }
}

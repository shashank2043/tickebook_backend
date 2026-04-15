package org.team11.tickebook.theatreservice.clients.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.team11.tickebook.theatreservice.clients.AdminClient;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;

import java.util.List;
import java.util.UUID;

@Component
public class AdminClientFallback implements AdminClient {

    @Override
    public ResponseEntity<Boolean> createRequest(TheatreApprovalRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(false);
    }

    @Override
    public ResponseEntity<List<TheatreApprovalResponseDto>> checkStatus(UUID theatreId) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of());
    }
}

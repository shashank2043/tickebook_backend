package org.team11.tickebook.theatreservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;

@FeignClient("ADMIN-SERVICE")
public interface AdminClient {
    @PostMapping("/api/theatre-approval-requests")
    public ResponseEntity<TheatreApprovalResponseDto> createRequest(
            @RequestBody TheatreApprovalRequestDto requestDto
    );
}

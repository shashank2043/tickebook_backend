package org.team11.tickebook.theatreservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ADMIN-SERVICE",configuration = FeignConfig.class)
public interface AdminClient {
    @PostMapping("/internal/theatre-approval")
    public ResponseEntity<Boolean> createRequest(
            @RequestBody TheatreApprovalRequestDto requestDto
    );
    @GetMapping("/internal/theatre-approval")
    public ResponseEntity<List<TheatreApprovalResponseDto>> checkStatus(@RequestParam UUID theatreId);
}

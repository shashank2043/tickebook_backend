package org.team11.tickebook.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "THEATRE-SERVICE")
public interface TheatreClient {

    @PutMapping("/internal/owner/{ownerProfileId}/verify")
    ResponseEntity<String> verifyOwner(
            @PathVariable UUID ownerProfileId
    );
}
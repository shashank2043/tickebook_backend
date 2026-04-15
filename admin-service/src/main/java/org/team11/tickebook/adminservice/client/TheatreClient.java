package org.team11.tickebook.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.team11.tickebook.adminservice.client.fallback.TheatreClientFallback;

import java.util.UUID;

@FeignClient(name = "THEATRE-SERVICE", fallback = TheatreClientFallback.class)
public interface TheatreClient {

    @PutMapping("/internal/owner/{ownerProfileId}/verify")
    ResponseEntity<String> verifyOwner(
            @PathVariable UUID ownerProfileId
    );
}
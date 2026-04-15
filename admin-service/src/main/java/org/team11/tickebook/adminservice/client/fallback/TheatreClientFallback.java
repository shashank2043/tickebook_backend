package org.team11.tickebook.adminservice.client.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.team11.tickebook.adminservice.client.TheatreClient;

import java.util.UUID;

@Component
public class TheatreClientFallback implements TheatreClient {

    @Override
    public ResponseEntity<String> verifyOwner(UUID ownerProfileId) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Theatre service is unavailable");
    }
}

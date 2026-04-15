package org.team11.tickebook.adminservice.client.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.team11.tickebook.adminservice.client.AuthClient;
import org.team11.tickebook.adminservice.model.Role;

import java.util.UUID;

@Component
public class AuthClientFallback implements AuthClient {

    @Override
    public ResponseEntity<String> updateUserRole(UUID userId, Role role) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Auth service is unavailable");
    }
}

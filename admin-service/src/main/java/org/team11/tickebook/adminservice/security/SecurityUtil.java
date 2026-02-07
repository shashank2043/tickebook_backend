package org.team11.tickebook.adminservice.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtil {
    public UUID getUserId(Authentication auth) {
        Claims claims = (Claims) auth.getPrincipal();
        return UUID.fromString(claims.get("userId").toString());
    }
}

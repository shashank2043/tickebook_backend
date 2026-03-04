package org.team11.tickebook.commondtos.authservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean success;
    private String userId;
    private String token;
    private String message;
}

package org.team11.tickebook.auth_service.dto.response;

import org.team11.tickebook.auth_service.model.User;

public class RegistrationResult {
    private User user;
    private String token;

    public RegistrationResult(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}

package org.team11.tickebook.auth_service.dto.response;

import java.util.UUID;
public class RegistrationResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private boolean emailVerified;

    public RegistrationResponse(UUID id, String email, String firstName, String lastName, String role, boolean emailVerified) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.emailVerified = emailVerified;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}

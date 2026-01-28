package org.team11.tickebook.adminservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class RoleElevationRequest {
    @Id
    private UUID id;
    private UUID requestedBy;

}

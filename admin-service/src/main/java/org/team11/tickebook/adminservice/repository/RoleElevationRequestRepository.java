package org.team11.tickebook.adminservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.adminservice.model.RoleElevationRequest;

import java.util.UUID;

@Repository
public interface RoleElevationRequestRepository extends JpaRepository<RoleElevationRequest, UUID> {
}

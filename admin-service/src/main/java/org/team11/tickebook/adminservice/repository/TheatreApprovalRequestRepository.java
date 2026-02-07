package org.team11.tickebook.adminservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.adminservice.model.TheatreApprovalRequest;

import java.util.List;
import java.util.UUID;


@Repository
public interface TheatreApprovalRequestRepository extends JpaRepository<TheatreApprovalRequest, UUID> {
    List<TheatreApprovalRequest> findByTheaterId(UUID theatreId);
}

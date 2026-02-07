package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TheatreOwnerProfileRepository extends JpaRepository<TheatreOwnerProfile, UUID> {
    Optional<TheatreOwnerProfile> findByUserId(UUID userId);
}

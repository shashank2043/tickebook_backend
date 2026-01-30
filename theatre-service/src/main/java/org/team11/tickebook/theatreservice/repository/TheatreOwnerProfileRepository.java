package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TheatreOwnerProfileRepository extends JpaRepository<org.team11.tickebook.theatreservice.model.TheatreOwnerProfile, UUID> {
}

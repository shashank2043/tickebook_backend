package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;
import java.util.UUID;

public interface TheatreRepository extends JpaRepository<Theatre, UUID> {
    List<Theatre> findByOwnerProfileId(UUID ownerId);
}

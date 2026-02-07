package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;

import java.util.List;
import java.util.UUID;

public interface TheatreRepository extends JpaRepository<Theatre, UUID> {
    List<Theatre> findByOwnerProfile(TheatreOwnerProfile profile);
    boolean existsByIdAndOwnerProfile(UUID theatreId, TheatreOwnerProfile profile);

}

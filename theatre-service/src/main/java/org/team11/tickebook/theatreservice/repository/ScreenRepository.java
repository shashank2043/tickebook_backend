package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {
//    List<Screen> findByTheatreId(UUID theatreId);
    List<Screen> findByTheatre(Theatre theatre);
}

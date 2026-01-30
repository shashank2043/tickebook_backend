package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.theatreservice.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {
}

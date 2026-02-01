package org.team11.tickebook.show_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.show_service.model.Show;

import java.util.UUID;

public interface ShowRepository extends JpaRepository<Show, UUID> {
}

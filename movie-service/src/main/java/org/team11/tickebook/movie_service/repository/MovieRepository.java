package org.team11.tickebook.movie_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.movie_service.model.Movie;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}

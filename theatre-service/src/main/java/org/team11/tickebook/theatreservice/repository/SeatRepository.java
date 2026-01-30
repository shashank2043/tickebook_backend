package org.team11.tickebook.theatreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.theatreservice.model.Seat;
@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
}

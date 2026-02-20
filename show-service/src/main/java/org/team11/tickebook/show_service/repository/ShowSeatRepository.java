package org.team11.tickebook.show_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team11.tickebook.show_service.model.ShowSeat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, UUID> {
    List<ShowSeat> findByShowId(UUID showId);
    void deleteByShowId(UUID showId);
    @Query("""
        SELECT s FROM ShowSeat s
        WHERE s.status = 'LOCKED'
        AND s.lockedAt <= :expiryTime
    """)
    List<ShowSeat> findExpiredLockedSeats(@Param("expiryTime") LocalDateTime expiryTime);
    Optional<ShowSeat> findByShowIdAndSeatId(UUID showID, Long seatId);
}
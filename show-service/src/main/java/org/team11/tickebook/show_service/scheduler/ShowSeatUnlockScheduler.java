package org.team11.tickebook.show_service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.model.ShowSeatStatus;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowSeatUnlockScheduler {

    private final ShowSeatRepository showSeatRepository;

    @Scheduled(fixedDelay = 60000) // runs every 1 minute
    @Transactional
    public void unlockExpiredSeats() {

        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);

        List<ShowSeat> expiredSeats =
                showSeatRepository.findExpiredLockedSeats(expiryTime);

        if (expiredSeats.isEmpty()) {
            return;
        }

        for (ShowSeat seat : expiredSeats) {
            seat.setStatus(ShowSeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seat.setLockedByUserId(null);
        }

        log.info("Unlocked {} expired seats", expiredSeats.size());
    }
}

package org.team11.tickebook.consumerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.consumerservice.model.BookingStatus;
import org.team11.tickebook.consumerservice.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private UUID id;
    private UUID userId;
    private UUID show;
    private String bookingNumber;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;
    private LocalDateTime bookedAt;
    private LocalDateTime expiresAt;
}

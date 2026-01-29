package org.team11.tickebook.consumerservice.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    private UUID userId;
    private UUID show;
    private BigDecimal totalAmount;
    private LocalDateTime expiresAt;
}


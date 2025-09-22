package com.app.presentation.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private UUID transactionId;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balance;
    private String accountNumber;
    private LocalDateTime createdAt;
}

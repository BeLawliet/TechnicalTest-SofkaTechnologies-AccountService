package com.app.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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

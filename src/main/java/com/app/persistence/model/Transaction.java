package com.app.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private UUID transactionId;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private ETransactionType transactionType;

    private BigDecimal amount;
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void init() {
        this.transactionId = UUID.randomUUID();
    }
}

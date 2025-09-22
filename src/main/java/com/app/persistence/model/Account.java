package com.app.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private EAccountType accountType;

    @Column(name = "initial_balance")
    private BigDecimal initialBalance;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}

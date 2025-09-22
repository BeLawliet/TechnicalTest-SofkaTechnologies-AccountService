package com.app.provider;

import com.app.persistence.model.*;
import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.SaveTransactionDTO;
import com.app.presentation.dto.TransactionDTO;
import com.app.presentation.dto.UpdateAccountDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class DataProvider {
    public static Account mockAccount() {
        return Account.builder()
                      .accountNumber("ACC001")
                      .accountType(EAccountType.SAVINGS)
                      .initialBalance(BigDecimal.valueOf(1000L))
                      .customerId(UUID.fromString("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f"))
                      .status(EStatus.ACTIVE)
                      .build();
    }

    public static AccountDTO mockAccountDTO() {
        return AccountDTO.builder()
                         .accountNumber("ACC001")
                         .accountType("SAVINGS")
                         .initialBalance(BigDecimal.valueOf(1000L))
                         .customerId("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f")
                         .status("ACTIVE")
                         .build();
    }

    public static UpdateAccountDTO mockUpdateAccountDTO() {
        return UpdateAccountDTO.builder()
                               .accountType(EAccountType.CHECKING)
                               .status(EStatus.INACTIVE)
                               .build();
    }

    public static Transaction mockTransaction() {
        return Transaction.builder()
                          .transactionId(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"))
                          .transactionType(ETransactionType.DEPOSIT)
                          .amount(BigDecimal.valueOf(500L))
                          .balance(BigDecimal.valueOf(1500L))
                          .account(mockAccount())
                          .createdAt(LocalDateTime.of(2025, 9, 7, 12, 0, 0))
                          .build();
    }

    public static TransactionDTO mockTransactionDTO() {
        return TransactionDTO.builder()
                             .transactionId(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"))
                             .transactionType("DEPOSIT")
                             .amount(BigDecimal.valueOf(500L))
                             .balance(BigDecimal.valueOf(1500L))
                             .accountNumber("ACC001")
                             .createdAt(LocalDateTime.of(2025, 9, 7, 12, 0, 0))
                             .build();
    }

    public static Transaction mockTransactionZero() {
        return Transaction.builder()
                          .transactionId(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"))
                          .transactionType(ETransactionType.WITHDRAWAL)
                          .amount(BigDecimal.valueOf(1000L))
                          .balance(BigDecimal.ZERO)
                          .account(mockAccount())
                          .createdAt(LocalDateTime.of(2025, 9, 7, 12, 0, 0))
                          .build();
    }

    public static SaveTransactionDTO mockSaveTransactionDTO() {
        return SaveTransactionDTO.builder()
                                 .transactionType(ETransactionType.DEPOSIT)
                                 .amount(BigDecimal.valueOf(500L))
                                 .accountNumber("ACC001")
                                 .build();
    }

    public static SaveTransactionDTO mockSaveTransactionDTOError() {
        return SaveTransactionDTO.builder()
                                 .transactionType(ETransactionType.WITHDRAWAL)
                                 .amount(BigDecimal.valueOf(1500L))
                                 .accountNumber("ACC001")
                                 .build();
    }

    public static SaveTransactionDTO mockSaveTransactionDTOZero() {
        return SaveTransactionDTO.builder()
                                 .transactionType(ETransactionType.WITHDRAWAL)
                                 .amount(BigDecimal.valueOf(1000L))
                                 .accountNumber("ACC001")
                                 .build();
    }
}

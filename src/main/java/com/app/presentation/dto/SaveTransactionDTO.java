package com.app.presentation.dto;

import com.app.persistence.model.ETransactionType;
import com.app.util.anotation.CheckTransaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@CheckTransaction(message = "El monto no es válido para el tipo de transacción")
public record SaveTransactionDTO(@NotNull(message = "El tipo de transacción es obligatorio") ETransactionType transactionType,
                                 @NotNull(message = "El saldo del movimiento es obligatorio") BigDecimal amount,
                                 @NotBlank(message = "El número de cuenta es obligatorio") String accountNumber) { }

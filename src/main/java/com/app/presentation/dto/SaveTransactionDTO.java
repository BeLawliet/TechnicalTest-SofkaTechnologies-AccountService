package com.app.presentation.dto;

import com.app.persistence.model.ETransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record SaveTransactionDTO(@NotNull(message = "El tipo de transacción es obligatorio") ETransactionType transactionType,
                                 @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a cero")
                                 @NotNull(message = "El saldo del movimiento es obligatorio") BigDecimal amount,
                                 @NotBlank(message = "El número de cuenta es obligatorio") String accountNumber) { }

package com.app.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String accountNumber;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String accountType;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El saldo inicial no puede ser negativo")
    private BigDecimal initialBalance;

    @NotBlank(message = "El cliente es obligatorio")
    private String customerId;

    @NotBlank(message = "El estado de la cuenta es obligatorio")
    private String status;
}

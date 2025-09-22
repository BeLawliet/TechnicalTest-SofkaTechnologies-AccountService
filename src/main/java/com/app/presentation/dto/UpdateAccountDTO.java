package com.app.presentation.dto;

import com.app.persistence.model.EAccountType;
import com.app.persistence.model.EStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateAccountDTO(@NotNull(message = "El tipo de cuenta es obligatorio") EAccountType accountType,
                               @NotNull(message = "El estado es obligatorio") EStatus status) { }

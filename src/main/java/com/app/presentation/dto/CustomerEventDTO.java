package com.app.presentation.dto;

import com.app.persistence.model.EStatus;
import lombok.Builder;
import java.util.UUID;

@Builder
public record CustomerEventDTO(ETypeEventDTO typeEvent, UUID customerId, String accountType, EStatus status) { }

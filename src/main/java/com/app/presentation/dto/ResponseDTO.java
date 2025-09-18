package com.app.presentation.dto;

import lombok.Builder;

@Builder
public record ResponseDTO(int status, String message, Object data) { }

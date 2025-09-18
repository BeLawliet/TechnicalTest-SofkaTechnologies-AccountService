package com.app.advice;

import com.app.presentation.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                                       .getFieldErrors().stream()
                                                        .collect(Collectors.toMap(
                                                                FieldError::getField,
                                                                FieldError::getDefaultMessage,
                                                                (existing, replacement) -> existing
                                                        ));

        ResponseDTO response = ResponseDTO.builder()
                                          .status(HttpStatus.BAD_REQUEST.value())
                                          .message("Error de validación")
                                          .data(errors)
                                          .build();

        return ResponseEntity.badRequest().body(response);
    }
}

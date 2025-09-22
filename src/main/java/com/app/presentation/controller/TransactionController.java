package com.app.presentation.controller;

import com.app.presentation.dto.ResponseDTO;
import com.app.presentation.dto.SaveTransactionDTO;
import com.app.presentation.dto.TransactionDTO;
import com.app.service.ITransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService transactionService;

    @GetMapping
    public ResponseEntity<ResponseDTO> findAllRegisters() {
        List<TransactionDTO> registers = this.transactionService.findAll();

        ResponseDTO response = ResponseDTO.builder()
                                          .status(HttpStatus.OK.value())
                                          .message("Se cargaron todos los movimientos")
                                          .data(registers)
                                          .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveRegister(@Valid @RequestBody SaveTransactionDTO request) {
        Optional<TransactionDTO> register = this.transactionService.save(request);

        return register.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                                                                                                             .status(HttpStatus.CREATED.value())
                                                                                                             .message("Movimiento realizado correctamente")
                                                                                                             .data(value)
                                                                                                             .build())
                           )
                       .orElseGet(() -> ResponseEntity.badRequest().body(ResponseDTO.builder()
                                                                                    .status(HttpStatus.BAD_REQUEST.value())
                                                                                    .message("No se pudo realizar el movimiento")
                                                                                    .data(null)
                                                                                    .build()));
    }
}

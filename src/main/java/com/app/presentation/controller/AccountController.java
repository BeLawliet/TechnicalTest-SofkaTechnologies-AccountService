package com.app.presentation.controller;

import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.ResponseDTO;
import com.app.presentation.dto.UpdateAccountDTO;
import com.app.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @GetMapping
    public ResponseEntity<ResponseDTO> findAllRegisters() {
        List<AccountDTO> registers = this.accountService.findAll();

        ResponseDTO response = ResponseDTO.builder()
                                          .status(HttpStatus.OK.value())
                                          .message("Se cargaron todas las cuentas")
                                          .data(registers)
                                          .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveRegister(@Valid @RequestBody AccountDTO request) {
        AccountDTO register = this.accountService.save(request);

        ResponseDTO response = ResponseDTO.builder()
                                          .status(HttpStatus.CREATED.value())
                                          .message("Cuenta guardada correctamente")
                                          .data(register)
                                          .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "{accountNumber}")
    public ResponseEntity<ResponseDTO> updateRegister(@PathVariable String accountNumber, @Valid @RequestBody UpdateAccountDTO request) {
        Optional<AccountDTO> register = this.accountService.update(accountNumber, request);

        return register.map(value -> ResponseEntity.ok(ResponseDTO.builder()
                                                                             .status(HttpStatus.OK.value())
                                                                             .message("Cuenta actualizada correctamente")
                                                                             .data(value)
                                                                             .build()
                                                                  )
                           )
                       .orElseGet(() -> ResponseEntity.badRequest().body(ResponseDTO.builder()
                                                                                    .status(HttpStatus.BAD_REQUEST.value())
                                                                                    .message(String.format("El número de cuenta %s no esta registrado", accountNumber))
                                                                                    .data(null)
                                                                                    .build()
                       ));
    }

    @DeleteMapping(path = "{accountNumber}")
    public ResponseEntity<String> deleteRegister(@PathVariable String accountNumber) {
        boolean wasDeleted = this.accountService.delete(accountNumber);

        if (!wasDeleted) return ResponseEntity.badRequest().body(String.format("El número de cuenta %s no esta registrado", accountNumber));

        return ResponseEntity.ok("Cuenta eliminada correctamente");
    }
}

package com.app.presentation.controller;

import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.ResponseDTO;
import com.app.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}

package com.app.presentation.controller;

import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.UpdateAccountDTO;
import com.app.service.IAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static com.app.provider.DataProvider.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAccountService accountService;

    @Test
    void should_return_ok_with_accounts_when_service_has_data() throws Exception {
        when(this.accountService.findAll()).thenReturn(List.of(mockAccountDTO()));

        mockMvc.perform(get("/api/v1/accounts"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.message").value("Se cargaron todas las cuentas"))
               .andExpect(jsonPath("$.data.length()").value(1))
               .andExpect(jsonPath("$.data[0].accountNumber").value("ACC001"))
               .andExpect(jsonPath("$.data[0].status").value("ACTIVE"));
    }

    @Test
    void should_return_ok_when_save_account() throws Exception {
        AccountDTO accountDTO = mockAccountDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(accountDTO);

        when(this.accountService.save(any(AccountDTO.class))).thenReturn(accountDTO);

        mockMvc.perform(post("/api/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                            .content(jsonRequest))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
               .andExpect(jsonPath("$.message").value("Cuenta guardada correctamente"))
               .andExpect(jsonPath("$.data.initialBalance").value(BigDecimal.valueOf(1000L)))
               .andExpect(jsonPath("$.data.customerId").value("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f"));
    }

    @Test
    void should_return_ok_when_update_account() throws Exception {
        String accountNumber = "ACC001";
        UpdateAccountDTO updateAccountDTO = mockUpdateAccountDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(updateAccountDTO);

        when(this.accountService.update("ACC001", updateAccountDTO)).thenReturn(Optional.of(mockAccountDTO()));

        mockMvc.perform(put("/api/v1/accounts/{accountNumber}", accountNumber).contentType(MediaType.APPLICATION_JSON)
                                                                                          .content(jsonRequest))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.message").value("Cuenta actualizada correctamente"))
               .andExpect(jsonPath("$.data.accountNumber").value("ACC001"))
               .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void should_return_error_when_update_id_incorrect() throws Exception {
        String accountNumber = "ACC001";
        UpdateAccountDTO updateAccountDTO = mockUpdateAccountDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(updateAccountDTO);

        when(this.accountService.update(eq("ACC001"), any(UpdateAccountDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/accounts/{accountNumber}", accountNumber).contentType(MediaType.APPLICATION_JSON)
                                                                                          .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath("$.message").value("El número de cuenta ACC001 no esta registrado"));
    }

    @Test
    void should_return_ok_when_delete_account() throws Exception {
        String expectedMessage = "Cuenta eliminada correctamente";

        when(this.accountService.delete("ACC001")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/accounts/{accountNumber}", "ACC001"))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedMessage));
    }

    @Test
    void should_return_error_when_delete_id_account_incorrect() throws Exception {
        String accountNumber = "ACC001";
        String expectedMessage = "El número de cuenta ACC001 no esta registrado";

        when(this.accountService.delete(accountNumber)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/accounts/{accountNumber}", accountNumber))
               .andExpect(status().isBadRequest())
               .andExpect(content().string(expectedMessage));
    }
}

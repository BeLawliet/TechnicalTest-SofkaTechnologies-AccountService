package com.app.presentation.controller;

import com.app.presentation.dto.SaveTransactionDTO;
import com.app.service.ITransactionService;
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
class TransacctionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ITransactionService transactionService;

    @Test
    void should_return_ok_with_transactions_when_service_has_data() throws Exception {
        when(this.transactionService.findAll()).thenReturn(List.of(mockTransactionDTO()));

        mockMvc.perform(get("/api/v1/transactions"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.message").value("Se cargaron todos los movimientos"))
               .andExpect(jsonPath("$.data.length()").value(1))
               .andExpect(jsonPath("$.data[0].transactionType").value("DEPOSIT"))
               .andExpect(jsonPath("$.data[0].accountNumber").value("ACC001"));
    }

    @Test
    void should_return_ok_when_save_transaction() throws Exception {
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(saveTransactionDTO);

        when(this.transactionService.save(saveTransactionDTO)).thenReturn(Optional.of(mockTransactionDTO()));

        mockMvc.perform(post("/api/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                                                                .content(jsonRequest))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
               .andExpect(jsonPath("$.message").value("Movimiento realizado correctamente"))
               .andExpect(jsonPath("$.data.transactionId").value("0a1b2c3d-4e5f-6789-0abc-def012345678"))
               .andExpect(jsonPath("$.data.balance").value(BigDecimal.valueOf(1500L)));
    }

    @Test
    void should_return_error_when_save_parameters_incorrect() throws Exception {
        SaveTransactionDTO saveTransactionDTO = SaveTransactionDTO.builder()
                                                                  .transactionType(null)
                                                                  .amount(null)
                                                                  .build();
        String jsonRequest = this.objectMapper.writeValueAsString(saveTransactionDTO);

        when(this.transactionService.save(saveTransactionDTO)).thenReturn(Optional.of(mockTransactionDTO()));

        mockMvc.perform(post("/api/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                                                                .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath("$.message").value("Error de validación"))
               .andExpect(jsonPath("$.data.transactionType").value("El tipo de transacción es obligatorio"))
               .andExpect(jsonPath("$.data.amount").value("El saldo del movimiento es obligatorio"));
    }

    @Test
    void should_return_error_when_save_id_incorrect() throws Exception {
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(saveTransactionDTO);

        when(this.transactionService.save(saveTransactionDTO)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                                                                .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath("$.message").value("No se pudo realizar el movimiento"));
    }

    @Test
    void should_return_exception_when_save_transaction() throws Exception {
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTO();
        String jsonRequest = this.objectMapper.writeValueAsString(saveTransactionDTO);

        when(this.transactionService.save(saveTransactionDTO)).thenThrow(new IllegalArgumentException("Saldo insuficiente para realizar el retiro"));

        mockMvc.perform(post("/api/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                                                                .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath("$.message").value("Saldo insuficiente para realizar el retiro"));
    }

    @Test
    void should_internal_server_error() throws Exception {
        when(this.transactionService.findAll()).thenThrow(new RuntimeException("DB down"));

        mockMvc.perform(get("/api/v1/transactions"))
               .andExpect(status().isInternalServerError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
               .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado, contacte al administrador"));
    }
}

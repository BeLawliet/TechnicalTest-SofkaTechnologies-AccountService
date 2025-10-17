package com.app.presentation.controller;

import com.app.service.IAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static com.app.provider.DataProvider.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
}

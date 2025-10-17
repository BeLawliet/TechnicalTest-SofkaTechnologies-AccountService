package com.app.service.impl;

import com.app.persistence.model.Account;
import com.app.persistence.repository.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static com.app.provider.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private IAccountRepository accountRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        this.accountService = new AccountServiceImpl(accountRepository, modelMapper);
    }

    @Test
    void should_return_accounts_when_repository_has_data() {
        // Arrange
        when(this.accountRepository.findAll()).thenReturn(List.of(mockAccount()));

        // Act
        var result = this.accountService.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(1, result.size()),
            () -> assertEquals("ACC001", result.get(0).getAccountNumber()),
            () -> assertEquals("ACTIVE", result.get(0).getStatus())
        );
    }

    @Test
    void should_save_account_when_parameters_correct() {
        // Arrange
        when(this.accountRepository.save(any(Account.class))).thenReturn(mockAccount());

        // Act
        this.accountService.save(mockCustomerEventDTO());

        // Assert
        verify(this.accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void should_update_or_delete_account_when_parameters_correct() {
        // Arrange
        UUID customerId = UUID.fromString("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f");
        Account account = mockAccount();

        when(this.accountRepository.findByCustomerId(customerId)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        this.accountService.updateAndDelete(mockCustomerEventDTO());

        // Assert
        verify(this.accountRepository, times(1)).save(account);
    }

    @Test
    void should_do_not_update_or_delete_account_when_customer_incorrect() {
        // Arrange
        UUID customerId = UUID.fromString("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f");

        when(this.accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.accountService.updateAndDelete(mockCustomerEventDTO()));
        assertEquals("El cliente indicado no tiene cuentas asociadas", exception.getMessage());
    }
}

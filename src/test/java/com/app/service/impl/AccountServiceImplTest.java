package com.app.service.impl;

import com.app.persistence.model.Account;
import com.app.persistence.repository.IAccountRepository;
import com.app.presentation.dto.UpdateAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static com.app.provider.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private IAccountRepository accountRepository;

    private ModelMapper modelMapper = new ModelMapper();

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
        var result = this.accountService.save(mockAccountDTO());

        // Assert
        verify(this.accountRepository, times(1)).save(any(Account.class));

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("SAVINGS", result.getAccountType()),
            () -> assertEquals(BigDecimal.valueOf(1000L), result.getInitialBalance())
        );
    }

    @Test
    void should_update_account_when_parameters_correct() {
        // Arrange
        String accountNumber = "ACC001";
        Account account = mockAccount();

        when(this.accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        var result = this.accountService.update(accountNumber, mockUpdateAccountDTO());

        // Assert
        verify(this.accountRepository, times(1)).save(account);

        assertAll(
            () -> assertFalse(result.isEmpty()),
            () -> assertEquals("ACC001", result.get().getAccountNumber()),
            () -> assertEquals("6f1d7f5e-9c8a-4b9e-8c3f-1a2b3c4d5e6f", result.get().getCustomerId()),
            () -> assertEquals("CHECKING", result.get().getAccountType()),
            () -> assertEquals("INACTIVE", result.get().getStatus())
        );
    }

    @Test
    void should_do_not_update_account_when_id_incorrect() {
        // Arrange
        String accountNumber = "0";
        when(this.accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // Act
        var result = this.accountService.update(accountNumber, any(UpdateAccountDTO.class));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void should_delete_account_when_id_correct() {
        // Arrange
        String accountNumber = "ACC001";
        Account account = mockAccount();

        when(this.accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));

        // Act
        var result = this.accountService.delete(accountNumber);

        // Assert
        verify(this.accountRepository).delete(account);
        assertTrue(result);
    }

    @Test
    void should_do_not_delete_account_when_id_incorrect() {
        // Arrange
        String accountNumber = "0";
        when(this.accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // Act
        var result = this.accountService.delete(accountNumber);

        // Assert
        assertFalse(result);
    }
}

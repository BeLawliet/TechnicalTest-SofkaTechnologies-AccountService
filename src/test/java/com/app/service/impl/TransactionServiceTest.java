package com.app.service.impl;

import com.app.persistence.model.ETransactionType;
import com.app.persistence.model.Transaction;
import com.app.persistence.repository.IAccountRepository;
import com.app.persistence.repository.ITransactionRepository;
import com.app.presentation.dto.SaveTransactionDTO;
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
import java.util.UUID;
import static org.mockito.Mockito.*;
import static com.app.provider.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IAccountRepository accountRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        this.transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, modelMapper);
    }

    @Test
    void should_return_transactions_when_repository_has_data() {
        // Arrange
        when(this.transactionRepository.findAll()).thenReturn(List.of(mockTransaction()));

        // Act
        var result = this.transactionService.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(1, result.size()),
            () -> assertEquals(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"), result.get(0).getTransactionId()),
            () -> assertEquals(BigDecimal.valueOf(1500L), result.get(0).getBalance())
        );
    }

    @Test
    void should_save_transactions_when_parameters_correct() {
        // Arrange
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTO();

        when(this.accountRepository.findById(saveTransactionDTO.accountNumber())).thenReturn(Optional.of(mockAccount()));
        when(this.transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction());

        // Act
        var result = this.transactionService.save(saveTransactionDTO);

        // Assert
        verify(this.transactionRepository, times(1)).save(any(Transaction.class));

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"), result.get().getTransactionId()),
            () -> assertEquals("DEPOSIT", result.get().getTransactionType()),
            () -> assertEquals(BigDecimal.valueOf(1500L), result.get().getBalance()),
            () -> assertEquals("ACC001", result.get().getAccountNumber())
        );
    }

    @Test
    void should_set_zero_when_amount_equals_balance() {
        // Arrange
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTOZero();

        when(this.accountRepository.findById(saveTransactionDTO.accountNumber())).thenReturn(Optional.of(mockAccount()));
        when(this.transactionRepository.save(any(Transaction.class))).thenReturn(mockTransactionZero());

        // Act
        var result = this.transactionService.save(saveTransactionDTO);

        // Assert
        verify(this.transactionRepository, times(1)).save(any(Transaction.class));

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(UUID.fromString("0a1b2c3d-4e5f-6789-0abc-def012345678"), result.get().getTransactionId()),
            () -> assertEquals("WITHDRAWAL", result.get().getTransactionType()),
            () -> assertEquals(BigDecimal.ZERO, result.get().getBalance())
        );
    }

    @Test
    void should_return_exception_when_do_not_have_balance_account() {
        // Arrange
        SaveTransactionDTO saveTransactionDTO = mockSaveTransactionDTOError();

        when(this.accountRepository.findById(saveTransactionDTO.accountNumber())).thenReturn(Optional.of(mockAccount()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.transactionService.save(saveTransactionDTO));
        assertEquals("Saldo insuficiente para realizar el retiro", exception.getMessage());
    }

    @Test
    void should_do_not_save_transactions_when_id_incorrect() {
        // Arrange
        when(this.accountRepository.findById(mockSaveTransactionDTO().accountNumber())).thenReturn(Optional.empty());

        // Act
        var result = this.transactionService.save(mockSaveTransactionDTO());

        // Assert
        assertTrue(result.isEmpty());
    }
}

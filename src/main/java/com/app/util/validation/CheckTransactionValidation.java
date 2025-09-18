package com.app.util.validation;

import com.app.presentation.dto.SaveTransactionDTO;
import com.app.util.anotation.CheckTransaction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class CheckTransactionValidation implements ConstraintValidator<CheckTransaction, SaveTransactionDTO> {
    @Override
    public boolean isValid(SaveTransactionDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        String errorMessage = null;

        if (value.amount() == null || value.amount().compareTo(BigDecimal.ZERO) == 0) {
            valid = false;
            errorMessage = "El monto del movimiento no puede ser cero";
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode("amount")
                    .addConstraintViolation();
        }

        return valid;
    }
}

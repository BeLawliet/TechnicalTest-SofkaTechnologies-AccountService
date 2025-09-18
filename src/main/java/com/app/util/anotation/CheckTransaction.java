package com.app.util.anotation;

import com.app.util.validation.CheckTransactionValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckTransactionValidation.class)
public @interface CheckTransaction {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

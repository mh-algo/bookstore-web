package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailCheckValidator.class)
public @interface EmailCheck {
    String message() default "{EmailCheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String emailId();
    String emailAddress();
}

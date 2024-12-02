package com.bookshelf.bookproject.seller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DiscountValidator.class)
public @interface ValidDiscount {
    String message() default "{invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String price();
    String discount();
}

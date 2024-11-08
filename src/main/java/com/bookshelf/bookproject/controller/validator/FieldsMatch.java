package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 두 필드의 값이 같은지 검증하는 어노테이션
 * <p> 두 필드의 값이 동일한지 검증할 때 클래스 레벨에 사용합니다.
 * 비교할 필드의 이름을 {@code first}와 {@code second}에 입력합니다.
 * <p> {@code message}를 통해 메시지를 설정할 수 있습니다.
 * 해당 메시지는 두 필드의 값이 일치하지 않을 때 표시됩니다.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldsMatchValidator.class)
public @interface FieldsMatch {
    String message() default "{FieldsMatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first();
    String second();
}

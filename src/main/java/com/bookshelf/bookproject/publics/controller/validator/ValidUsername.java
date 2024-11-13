package com.bookshelf.bookproject.publics.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 아이디의 패턴 및 중복 여부를 검증하는 어노테이션
 * <p> 검증할 아이디가 담긴 필드나 파라미터에 사용합니다.
 * <p> {@code message}를 통해 검증 실패 시 표시할 메시지를 설정할 수 있습니다.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUsernameValidator.class)
public @interface ValidUsername {
    String message() default "{ValidUsername.default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

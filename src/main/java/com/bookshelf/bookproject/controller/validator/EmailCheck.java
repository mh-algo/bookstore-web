package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이메일의 형식이 적절한지 검증하는 어노테이션
 * <p> 두 필드에 담긴 이메일 아이디와 이메일 주소가 적절한 형식인지 검증할 때 클래스 레벨에 사용합니다.
 * 검증할 필드의 이름을 {@code emailId}와 {@code emailAddress}에 입력합니다.
 * <p> {@code message}를 통해 메시지를 설정할 수 있습니다.
 * 해당 메시지는 두 필드의 값이 일치하지 않을 때 표시됩니다.
 */
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

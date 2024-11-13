package com.bookshelf.bookproject.publics.controller.validator;

import com.bookshelf.bookproject.publics.service.SignupService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private final SignupService signupService;

    public ValidUsernameValidator(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * 주어진 {@code username}을 검증
     * <p>{@code username}이 null일 경우 false를 반환합니다.
     * <p>{@code username}이 패턴을 만족하지 못하면 메시지를 추가하고 false를 반환합니다.
     * <p>{@code username}이 이미 존재할 경우 메시지를 추가하고 false를 반환합니다.
     * <p> 위의 모든 조건을 충족할 경우 true를 반환합니다.
     *
     * @param username 검증할 아이디 문자열
     * @param context 검증 작업에 필요한 컨텍스트 정보가 포함된 객체
     * @return {@code username}이 null, 패턴 미충족, 중복일 경우 각각 false를 반환하고, 모든 조건을 충족할 경우 true를 반환
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        boolean enableUsername = Pattern.matches("^[a-zA-Z0-9]{6,20}$", username);
        if (!enableUsername) {
            addConstraintValidator(context, "{ValidUsername.patternCheck}");
            return false;
        }
        enableUsername = signupService.isEnableUsername(username);
        if (!enableUsername) {
            addConstraintValidator(context, "{ValidUsername.uniqueCheck}");
            return false;
        }
        return true;
    }

    /**
     * 검증 실패 시 사용자 정의 메시지를 설정합니다.
     * <p>기본 검증 메시지를 비활성화하고, 주어진 메시지를 새로운 검증 메시지로 설정합니다.
     *
     * @param constraintValidatorContext 검증 작업에 필요한 컨텍스트 정보가 포함된 객체
     * @param message 추가할 사용자 정의 메시지
     */
    private static void addConstraintValidator(ConstraintValidatorContext constraintValidatorContext, String message) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}

package com.bookshelf.bookproject.controller.validator;

import com.bookshelf.bookproject.service.SignupService;
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

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        boolean enableUsername = Pattern.matches("^[a-zA-Z0-9]{6,20}$", value);
        if (!enableUsername) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("{ValidUsername.patternCheck}")
                    .addConstraintViolation();
            return false;
        }
        enableUsername = signupService.isEnableUsername(value);
        if (!enableUsername) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("{ValidUsername.uniqueCheck}")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

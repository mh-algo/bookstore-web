package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class EmailCheckValidator implements ConstraintValidator<EmailCheck, Object> {
    private String emailIdFieldName;
    private String emailAddressFieldName;

    @Override
    public void initialize(EmailCheck constraintAnnotation) {
        this.emailIdFieldName = constraintAnnotation.emailId();
        this.emailAddressFieldName = constraintAnnotation.emailAddress();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object emailIdObject = getFieldValue(object, emailIdFieldName);
            Object emailAddressObject = getFieldValue(object, emailAddressFieldName);

            if (emailIdObject instanceof String emailId && emailAddressObject instanceof String emailAddress) {
                return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", emailId + "@" + emailAddress);
            }

            return false;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}

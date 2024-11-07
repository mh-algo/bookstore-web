package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object firstValue = getFieldValue(object, firstFieldName);
            Object secondValue = getFieldValue(object, secondFieldName);

            return Objects.equals(firstValue, secondValue);
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

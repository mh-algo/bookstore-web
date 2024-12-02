package com.bookshelf.bookproject.seller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiscountValidator implements ConstraintValidator<ValidDiscount, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(ValidDiscount constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.price();
        this.secondFieldName = constraintAnnotation.discount();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object firstValue = getFieldValue(object, firstFieldName);
            Object secondValue = getFieldValue(object, secondFieldName);

            if (firstValue instanceof Integer price && secondValue instanceof Integer discount) {
                return price >= discount;
            }
            return false;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String methodName = getMethodName(fieldName);
        try {
            Method declaredMethod = object.getClass().getDeclaredMethod(methodName);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(object);
        } catch (NoSuchMethodException e) {
            Method publicMethod = object.getClass().getMethod(methodName);
            return publicMethod.invoke(object);
        }
    }

    private static String getMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}

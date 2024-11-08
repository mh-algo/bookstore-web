package com.bookshelf.bookproject.controller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    /**
     * 비교할 필드의 이름을 초기화
     * <p> {@link FieldsMatch} 어노테이션에서 입력받은 {@code first}와 {@code second} 값을
     * 각각 {@code firstFieldName}과 {@code secondFieldName}으로 설정합니다.
     * @param constraintAnnotation {@link FieldsMatch} 어노테이션 인스턴스로, 비교할 필드의 이름을 포함합니다.
     */
    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    /**
     * 필드의 값을 비교 후 검증
     * <p> 두 필드의 값이 같을 경우 true, 다를 경우 false를 반환합니다.
     * @param object 검증할 객체로, 비교할 필드를 포함합니다.
     * @param constraintValidatorContext 검증 작업에 필요한 컨텍스트 정보가 포함된 객체입니다.
     * @return 두 필드의 값이 같을 경우 true, 다를 경우 false, {@link ReflectiveOperationException}이 발생하면 false
     */
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

    /**
     * 객체에서 지정된 필드의 값을 반환
     * <p> 주어진 객체에서 주어진 필드 이름에 해당하는 필드 값을 찾아 반환합니다.
     * @param object 필드 값을 가져올 대상 객체
     * @param fieldName 값을 가져올 필드의 이름
     * @return 지정된 필드 이름에 해당하는 필드의 값
     * @throws NoSuchFieldException 지정된 필드 이름이 존재하지 않을 때 발생
     * @throws IllegalAccessException 필드에 접근할 수 없는 경우 발생
     */
    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}

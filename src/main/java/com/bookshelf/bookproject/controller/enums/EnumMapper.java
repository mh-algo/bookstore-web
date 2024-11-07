package com.bookshelf.bookproject.controller.enums;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumMapper {
    private final Map<String, List<EnumMapperValue>> factory = new LinkedHashMap<>();

    /**
     * 주어진 {@link EnumType} 클래스를 {@link EnumMapperValue} 객체 리스트로 변환하여,
     * 주어진 key와 함께 {@code key}와 {@code value} 형태로 저장
     *
     * @param key 저장할 {@link EnumType}에 대응되는 key
     * @param enumType 저장할 {@link EnumType} 클래스
     */
    public void put(String key, Class<? extends EnumType> enumType) {
        factory.put(key, toEnumValue(enumType));
    }

    /**
     * 주어진 {@link EnumType}을 {@link EnumMapperValue} 객체 리스트로 변환
     *
     * @param enumType 변환할 {@link EnumType} 클래스
     * @return 변환된 {@link EnumMapperValue} 객체 리스트
     */
    private List<EnumMapperValue> toEnumValue(Class<? extends EnumType> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .map(EnumMapperValue::new)
                .toList();
    }

    /**
     * 주어진 key에 해당하는 {@link EnumMapperValue} 리스트를 반환
     * @param key 반환받을 {@link EnumMapperValue} 리스트에 대응되는 key
     * @return 주어진 key에 해당하는 {@link EnumMapperValue} 리스트.
     * key에 해당하는 값이 없을 경우 {@code null}을 반환
     */
    public List<EnumMapperValue> get(String key) {
        return factory.get(key);
    }
}

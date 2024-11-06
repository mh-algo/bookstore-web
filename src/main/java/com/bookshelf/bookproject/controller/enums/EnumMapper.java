package com.bookshelf.bookproject.controller.enums;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumMapper {
    private final Map<String, List<EnumMapperValue>> factory = new LinkedHashMap<>();

    public void put(String key, Class<? extends EnumType> enumType) {
        factory.put(key, toEnumValue(enumType));
    }

    private List<EnumMapperValue> toEnumValue(Class<? extends EnumType> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .map(EnumMapperValue::new)
                .toList();
    }

    public List<EnumMapperValue> get(String key) {
        return factory.get(key);
    }
}

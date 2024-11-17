package com.bookshelf.bookproject.enums;

import java.util.*;
import java.util.stream.Collectors;

public class EnumMapper {
    private final Map<String, List<EnumMapperValue>> enumMap;

    /**
     * 주어진 enum 클래스들을 기반으로 {@code EnumMapper} 객체를 생성
     * <p> 이 생성자는 제공된 enum 클래스들을 매핑하여, 각 클래스 이름을 key로, 해당 enum 값 리스트를 value로 갖는
     * 변경 불가능한 Map을 초기화합니다.
     *
     * @param enumClasses enum 클래스의 이름과 그 타입을 매핑한 {@link Map} 객체
     */
    public EnumMapper(Map<String, Class<? extends EnumType>> enumClasses) {
        this.enumMap = enumClasses.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                        entry -> toEnumValue(entry.getValue()),
                        (v1, v2) -> v1));
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
        return enumMap.get(key);
    }
}

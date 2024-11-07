package com.bookshelf.bookproject.controller.enums;

public class EnumMapperValue {
    private final String code;
    private final String title;

    /**
     * 주어진 {@link EnumType} 객체에서 {@code code}와 {@code title}을 추출하여,
     * 각각 {@link String} 타입의 {@code code}와 {@code title} 필드에 저장
     *
     * @param enumType {@code code}와 {@code title}을 추출할 {@link EnumType} 객체
     */
    public EnumMapperValue(EnumType enumType) {
        this.code = enumType.getCode();
        this.title = enumType.getTitle();
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}

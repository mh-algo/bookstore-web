package com.bookshelf.bookproject.controller.enums;

public class EnumMapperValue {
    private final String code;
    private final String title;

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

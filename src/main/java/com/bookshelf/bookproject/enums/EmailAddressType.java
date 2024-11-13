package com.bookshelf.bookproject.enums;

public enum EmailAddressType implements EnumType {
    GMAIL("gmail.com"),
    NAVER("naver.com"),
    DAUM("daum.com");

    private final String address;

    private EmailAddressType(String address) {
        this.address = address;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return address;
    }
}

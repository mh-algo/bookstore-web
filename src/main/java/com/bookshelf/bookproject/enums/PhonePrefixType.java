package com.bookshelf.bookproject.enums;

public enum PhonePrefixType implements EnumType {
    ZERO("010"),
    ONE("011"),
    SIX("016"),
    SEVEN("017"),
    EIGHT("018"),
    NINE("019");

    private final String number;

    PhonePrefixType(String num) {
        this.number = num;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return number;
    }
}

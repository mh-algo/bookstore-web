package com.bookshelf.bookproject.controller.enums;

public enum LocalNumberType implements EnumType {
    SEOUL("02"),
    GYEONGGI("031"),
    INCHEON("032"),
    GANGWON("033"),
    CHUNGNAM("041"),
    DAEJEON("042"),
    CHUNGBUK("043"),
    SEJONG("044"),
    BUSAN("051"),
    ULSAN("052"),
    DAEGU("053"),
    GYEONGBUK("054"),
    GYEONGNAM("055"),
    JEONNAM("061"),
    GWANGJU("062"),
    JEONBUK("063"),
    JEJU("064"),
    INTERNET("070");

    private String number;

    LocalNumberType(String number) {
        this.number = number;
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

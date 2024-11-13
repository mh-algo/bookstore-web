package com.bookshelf.bookproject.controller.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Email {
    @Pattern(regexp = "^[A-Za-z0-9._%+-]{3,20}$")
    private String id;

    @Pattern(regexp = "^(?=.{4,20}$)[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String address;

    /**
     * select 태그 선택창 저장하는 용도
     */
    private String select = "";

    public String getEmail() {
        return id + "@" + address;
    }
}

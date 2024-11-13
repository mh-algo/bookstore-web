package com.bookshelf.bookproject.controller.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CsPhone {
    @Pattern(regexp = "^[0-9]{2,3}$")
    private String prefix;

    @Pattern(regexp = "^[0-9]{3,4}$")
    private String middle;

    @Pattern(regexp = "^[0-9]{4}$")
    private String last;

    public String getCsPhoneNumber() {
        return prefix + "-" + middle + "-" + last;
    }
}

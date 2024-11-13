package com.bookshelf.bookproject.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Phone {
    @NotBlank
    @Size(min = 3, max = 3)
    private String prefix;

    @Pattern(regexp = "^[0-9]{4}$")
    private String middle;

    @Pattern(regexp = "^[0-9]{4}$")
    private String last;

    public String getPhoneNumber() {
        return prefix + "-" + middle + "-" + last;
    }
}

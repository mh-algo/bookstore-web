package com.bookshelf.bookproject.publics.controller.dto.signup.item;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankInfo {
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$")
    private String name = "";

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$")
    private String depositor;

    @Pattern(regexp = "^[0-9]{10,14}$")
    private String accountNumber;
}

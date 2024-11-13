package com.bookshelf.bookproject.controller.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupSeller extends Signup {
    @Pattern(regexp = "^[0-9]{2,3}$")
    private String csPhonePrefix;

    @Pattern(regexp = "^[0-9]{3,4}$")
    private String csPhoneMiddle;

    @Pattern(regexp = "^[0-9]{4}$")
    private String csPhoneLast;

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$")
    private String bankName = "";

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$")
    private String depositor;

    @Pattern(regexp = "^[0-9]{10,14}$")
    private String accountNumber;
}

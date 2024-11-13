package com.bookshelf.bookproject.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupSeller extends Signup {
    @Valid
    private CsPhone csPhone;

    @Valid
    private BankInfo bankInfo;

    public String getCsPhoneNumber() {
        return csPhone.getCsPhoneNumber();
    }
}

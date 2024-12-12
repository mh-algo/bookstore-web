package com.bookshelf.bookproject.publicpage.controller.dto.signup;

import com.bookshelf.bookproject.publicpage.controller.dto.signup.item.BankInfo;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.item.CsPhone;
import jakarta.validation.Valid;
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

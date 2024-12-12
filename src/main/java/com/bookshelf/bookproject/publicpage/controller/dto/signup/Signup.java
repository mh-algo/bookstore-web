package com.bookshelf.bookproject.publicpage.controller.dto.signup;

import com.bookshelf.bookproject.publicpage.controller.dto.signup.item.Email;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.item.Phone;
import com.bookshelf.bookproject.publicpage.controller.validator.FieldsMatch;
import com.bookshelf.bookproject.publicpage.controller.validator.ValidUsername;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@FieldsMatch(first = "password", second = "passwordConfirm")
public class Signup {
    @ValidUsername
    private String username;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$")
    private String name;

    @Valid
    private Phone phone;

    @Valid
    private Email email;

    public String getPhoneNumber() {
        return phone.getPhoneNumber();
    }

    public String getEmailAddress() {
        return email.getEmail();
    }
}

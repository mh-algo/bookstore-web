package com.bookshelf.bookproject.controller.dto;

import com.bookshelf.bookproject.controller.validator.EmailCheck;
import com.bookshelf.bookproject.controller.validator.FieldsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsMatch(first = "password", second = "passwordConfirm")
@EmailCheck(emailId = "emailId", emailAddress = "emailAddress")
public class Signup {
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$")
    private String username;
//    private String usernameChk;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$")
    private String name;

    @NotBlank
    @Size(max = 3)
    private String phonePrefix;

    @Pattern(regexp = "^[0-9]{4}$")
    private String phoneMiddle;

    @Pattern(regexp = "^[0-9]{4}$")
    private String phoneLast;

    @NotBlank
    @Size(max = 20)
    private String emailId;

    @NotBlank
    @Size(max = 20)
    private String emailAddress;
}

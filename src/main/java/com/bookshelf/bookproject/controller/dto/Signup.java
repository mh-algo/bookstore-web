package com.bookshelf.bookproject.controller.dto;

import com.bookshelf.bookproject.controller.validator.FieldsMatch;
import com.bookshelf.bookproject.controller.validator.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, max = 3)
    private String phonePrefix;

    @Pattern(regexp = "^[0-9]{4}$")
    private String phoneMiddle;

    @Pattern(regexp = "^[0-9]{4}$")
    private String phoneLast;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]{3,20}$")
    private String emailId;

    @Pattern(regexp = "^(?=.{4,20}$)[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String emailAddress;

    /**
     * select 태그 선택창 저장하는 용도
     */
    private String selectEmail = "";
}

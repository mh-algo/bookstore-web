package com.bookshelf.bookproject.publics.controller.dto.signup;

import com.bookshelf.bookproject.publics.controller.validator.ValidUsername;
import lombok.Data;

@Data
public class Username {
    @ValidUsername
    private String username;
}

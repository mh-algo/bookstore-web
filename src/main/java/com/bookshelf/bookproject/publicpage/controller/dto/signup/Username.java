package com.bookshelf.bookproject.publicpage.controller.dto.signup;

import com.bookshelf.bookproject.publicpage.controller.validator.ValidUsername;
import lombok.Data;

@Data
public class Username {
    @ValidUsername
    private String username;
}

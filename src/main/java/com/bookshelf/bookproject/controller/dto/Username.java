package com.bookshelf.bookproject.controller.dto;

import com.bookshelf.bookproject.controller.validator.ValidUsername;
import lombok.Data;

@Data
public class Username {
    @ValidUsername
    private String username;
}

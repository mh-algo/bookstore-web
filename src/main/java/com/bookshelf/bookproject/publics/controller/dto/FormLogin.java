package com.bookshelf.bookproject.publics.controller.dto;

import lombok.Data;

@Data
public class FormLogin {
    private final String username;
    private final String password;
}
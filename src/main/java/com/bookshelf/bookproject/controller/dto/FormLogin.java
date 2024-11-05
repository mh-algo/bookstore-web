package com.bookshelf.bookproject.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class FormLogin {
    private final String username;
    private final String password;
}

package com.bookshelf.bookproject.controller.dto;

import lombok.Data;

@Data
public class Signup {
    private String username;
    private String password;
    private String passwordChk;
    private String name;
    private String phonePrefix;
    private String phoneMiddle;
    private String phoneLast;
    private String emailId;
    private String emailAddress;
}

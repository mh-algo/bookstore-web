package com.bookshelf.bookproject.publicpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    @Transactional
    public String index() {
        return "index";
    }
}

package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @Transactional
    public String index() {
        return "index";
    }
}

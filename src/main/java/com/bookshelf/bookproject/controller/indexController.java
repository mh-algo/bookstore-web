package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.domain.entity.AccountStatus;
import com.bookshelf.bookproject.domain.entity.User;
import com.bookshelf.bookproject.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {
    private final UserRepository userRepository;
    private final EntityManager em;

    public indexController(UserRepository userRepository, EntityManager em) {
        this.userRepository = userRepository;
        this.em = em;
    }

    @GetMapping("/")
    @ResponseBody
    @Transactional
    public String index() {
//        Users users = userRepository.findAll().get(0);
        /*User user = User.builder()
                .name("이름")
                .accountId("user")
                .email("user@sdsdds.com")
                .password("pwd")
                .status(AccountStatus.DELETED)
                .build();

        em.persist(user);*/

        return "login success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

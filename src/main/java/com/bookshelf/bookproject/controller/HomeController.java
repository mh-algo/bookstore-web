package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final AccountRepository accountRepository;
    private final EntityManager em;

    public HomeController(AccountRepository accountRepository, EntityManager em) {
        this.accountRepository = accountRepository;
        this.em = em;
    }

    @GetMapping("/")
    @ResponseBody
    @Transactional
    public String index() {
        /*Account user = accountRepository.findByAccountId("userid");

        System.out.println(
                user.getName() +
                user.getPassword() +
                user.getAccountId() +
                user.getStatus()
        );*/


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
}

package com.bookshelf.bookproject.seller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class DashBoardController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/main-dashboard";
    }
}

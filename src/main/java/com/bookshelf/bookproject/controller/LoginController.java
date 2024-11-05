package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.controller.dto.FormLogin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute("formLogin") FormLogin formLogin,
                        BindingResult bindingResult) {
        try {
            HttpSession session = request.getSession(false);
            String error = (String) session.getAttribute("error");
            switch (error) {
                case "UsernameBlankException" -> bindingResult.rejectValue("username", error);
                case "PasswordBlankException" -> bindingResult.rejectValue("password", error);
                case "BadCredentialsException", "UsernameNotFoundException", "DisabledException",
                     "AccountDeletedException" -> bindingResult.reject(error);
            }
            session.removeAttribute("error");
        } catch (NullPointerException ignored) {}

        return "login";
    }
}



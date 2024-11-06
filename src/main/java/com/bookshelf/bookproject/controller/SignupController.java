package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.controller.dto.Signup;
import com.bookshelf.bookproject.controller.enums.EnumMapper;
import com.bookshelf.bookproject.controller.enums.EnumMapperValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {
    private final EnumMapper enumMapper;

    private static final String EMAIL_ADDRESS_TYPE = "emailAddressType";
    private static final String PHONE_PREFIX_TYPE = "phonePrefixType";

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signup", new Signup());
        model.addAttribute(EMAIL_ADDRESS_TYPE, enumMapper.get(EMAIL_ADDRESS_TYPE));
        model.addAttribute(PHONE_PREFIX_TYPE, enumMapper.get(PHONE_PREFIX_TYPE));
        return "signup";
    }

    @PostMapping("/signup")
    public String saveAccount(@Valid @ModelAttribute("signup") Signup signup, BindingResult bindingResult) {
        System.out.println("signup = " + signup);
        if (bindingResult.hasErrors()) {
            return "redirect:/signup";
        }

        return "index";
    }
}

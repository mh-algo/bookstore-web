package com.bookshelf.bookproject.seller.controller;

import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/dashboard";
    }

    @GetMapping("/register")
    public String register(Model model) {
        addAllCategories(model);
        return "seller/product-registration";
    }

    private void addAllCategories(Model model) {
        List<CategoryDto> allCategories = managementService.getAllCategories();
        model.addAttribute("categories", allCategories);
    }
}

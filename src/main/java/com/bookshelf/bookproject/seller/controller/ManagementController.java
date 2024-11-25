package com.bookshelf.bookproject.seller.controller;

import com.bookshelf.bookproject.security.dto.AccountAuth;
import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.ManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String register(@ModelAttribute("registerInfo") RegisterInfo registerInfo, Model model) {
        addAllCategories(model);
        return "seller/product-registration";
    }

    @PostMapping("/register")
    public String saveRegisterInfo(@RequestPart("mainImageFile") MultipartFile mainImageFile,
                                   @RequestPart("additionalImageFiles") List<MultipartFile> additionalImageFiles,
                                   @Valid @ModelAttribute RegisterInfo registerInfo,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal AccountAuth accountAuth,
                                   Model model) {
        addAllCategories(model);
        addCategoryPath(model, registerInfo.getSelectedCategory());

        if (bindingResult.hasErrors()) {
                managementService.handleImageUpload(mainImageFile, registerInfo, accountAuth.getAccountId());
                managementService.handleImagesUpload(additionalImageFiles, registerInfo, accountAuth.getAccountId());

            return "seller/product-registration";
        }
        return "redirect:/seller/dashboard";
    }

    private static void addCategoryPath(Model model, SelectedCategory selectedCategory) {
        if (selectedCategory.categoryExists()) {
            model.addAttribute("selectedCategoryPath", selectedCategory.getCategoryPath());
        }
    }

    private void addAllCategories(Model model) {
        List<CategoryDto> allCategories = managementService.getAllCategories();
        model.addAttribute("categories", allCategories);
    }
}

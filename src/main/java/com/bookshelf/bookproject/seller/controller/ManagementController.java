package com.bookshelf.bookproject.seller.controller;

import com.bookshelf.bookproject.security.dto.AccountAuth;
import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Product;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.ManagementService;
import com.bookshelf.bookproject.seller.service.dto.SearchInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.bookshelf.bookproject.seller.service.ManagementService.*;

@Slf4j
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
    public String saveRegisterInfo(@RequestPart MultipartFile mainImageFile,
                                   @RequestPart List<MultipartFile> additionalImageFiles,
                                   @Valid @ModelAttribute RegisterInfo registerInfo,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal AccountAuth accountAuth,
                                   Model model) {
        addAllCategories(model);
        addCategoryPath(model, registerInfo.getSelectedCategory());

        managementService.handleImageUpload(mainImageFile, registerInfo, accountAuth.getAccountId());
        managementService.handleImagesUpload(additionalImageFiles, registerInfo, accountAuth.getAccountId());

        if (bindingResult.hasErrors() || !validateImageFile(mainImageFile) ||
                !validateImageFiles(additionalImageFiles) || additionalImageFiles.size() > 9) {
            return "seller/product-registration";
        }

        Product product = registerInfo.getProduct();
        SearchInfo searchInfo = managementService.requestSearchInfo(product.getIsbn());

        if (!validateBookData(searchInfo)) {
            log.warn("유효하지 않은 데이터입니다 : {}", registerInfo);
            return "seller/product-registration";
        }

        // discount가 price보다 크지 않아야 한다는 검증 추가 필요!!

        // 데이터 저장 로직 추가


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

    @PostMapping("/register/search-book")
    public ResponseEntity<String> searchBook(@RequestParam String bookName, @RequestParam Integer page) {
        String response = managementService.requestBookDataAsJson(bookName, page);
        return ResponseEntity.ok(response);
    }
}

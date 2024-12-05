package com.bookshelf.bookproject.seller.controller;

import com.bookshelf.bookproject.common.CategoryService;
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
import java.util.Map;

import static com.bookshelf.bookproject.common.CategoryService.validateCategory;
import static com.bookshelf.bookproject.seller.service.ManagementService.*;

@Slf4j
@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class ManagementController {
    private final CategoryService categoryService;
    private final ManagementService managementService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/dashboard";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute RegisterInfo registerInfo, Model model) {
        addCategoryMap(model);
        return "seller/product-registration";
    }

    @PostMapping("/register")
    public String saveRegisterInfo(@RequestPart MultipartFile mainImageFile,
                                   @RequestPart List<MultipartFile> additionalImageFiles,
                                   @Valid @ModelAttribute RegisterInfo registerInfo,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal AccountAuth accountAuth,
                                   Model model) {
        Map<Long, CategoryDto> categoryMap = addCategoryMap(model);     // 뷰에 보여줄 카테고리 추가

        // 클라이언트가 업로드한 이미지 파일 임시 저장
        String accountId = accountAuth.getAccountId();
        managementService.uploadAndSetAllImages(mainImageFile, additionalImageFiles, registerInfo, accountId);

        // 클라이언트가 선택한 카테고리 정보를 담은 객체
        SelectedCategory selectedCategory = registerInfo.getSelectedCategory();

        // 데이터 검증
        if (bindingResult.hasErrors() || !validateCategory(categoryMap, selectedCategory) ||
                !isValidImageFile(mainImageFile, additionalImageFiles)) {
            return "seller/product-registration";
        }

        // 클라이언트가 선택한 카테고리 경로 추가
        setSelectedCategory(categoryMap, selectedCategory);
        addCategoryPath(model, selectedCategory);

        // 입력 도서 데이터 검증
        Product product = registerInfo.getProduct();
        SearchInfo searchInfo = managementService.requestSearchInfo(product.getIsbn());
        if (!validateBookData(searchInfo)) {
            return "seller/product-registration";
        }

        // 입력 데이터 저장
        managementService.registerProduct(registerInfo, searchInfo.getItems(), accountId);
        managementService.saveAllImageFiles(registerInfo, accountId);    // 클라이언트가 업로드한 이미지 파일 최종 저장

        return "redirect:/seller/dashboard";
    }

    private static void addCategoryPath(Model model, SelectedCategory selectedCategory) {
        if (selectedCategory.categoryExists()) {
            model.addAttribute("selectedCategoryPath", selectedCategory.getCategoryPath());
        }
    }

    private Map<Long, CategoryDto> addCategoryMap(Model model) {
        Map<Long, CategoryDto> categoryMap = categoryService.getAllCategories();
        model.addAttribute("categoryMap", categoryMap);
        return categoryMap;
    }

    @PostMapping("/register/search-book")
    public ResponseEntity<String> searchBook(@RequestParam String bookName, @RequestParam Integer page) {
        String response = managementService.requestBookDataAsJson(bookName, page);
        return ResponseEntity.ok(response);
    }
}

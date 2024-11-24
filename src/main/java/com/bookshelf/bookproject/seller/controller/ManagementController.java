package com.bookshelf.bookproject.seller.controller;

import com.bookshelf.bookproject.security.dto.AccountAuth;
import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.ManagementService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @Value("${app.upload.dir}")
    private String defaultPath;

    @Value("${app.access.url}")
    private String defaultUrl;

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
    public String saveRegisterInfo(@RequestPart("mainImageFile") MultipartFile mainImageFile, @RequestPart("additionalImageFiles") List<MultipartFile> additionalImageFiles,
                                   @ModelAttribute RegisterInfo registerInfo, Model model, @AuthenticationPrincipal AccountAuth accountAuth, HttpSession session) {
        addAllCategories(model);
        addCategoryPath(registerInfo.getSelectedCategory(), model);

        String accessDir = getStoragePath(defaultUrl, accountAuth.getAccountId());
        String uploadDir = getStoragePath(defaultPath, accountAuth.getAccountId());

        if (!mainImageFile.isEmpty()) {
            String mainImageName = uploadImage(mainImageFile, session.getId(), uploadDir);
            String mainImageUrl = accessDir + mainImageName;
            Image mainImage = registerInfo.getImage();
            mainImage.setPath(mainImageUrl);
        }

        if (!additionalImageFiles.get(0).isEmpty()) {
            registerInfo.setImages(
                    additionalImageFiles.stream().map(imageFile -> {
                        String imageName = uploadImage(imageFile, session.getId(), uploadDir);
                        String imageUrl = accessDir + imageName;

                        Image image = new Image();
                        image.setName(imageFile.getOriginalFilename());
                        image.setPath(imageUrl);

                        return image;
                    }).toList()
            );
        }

        return "seller/product-registration";
    }

    private static String getStoragePath(String path, String accountId) {
        return path + accountId + "/tmp/";
    }

    private String uploadImage(MultipartFile file, String sessionId, String uploadDir) {
        if (file.isEmpty()) {
            return null;    // 나중에 custom exception throw
        }

        try {
            File userDir = new File(uploadDir);
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String saveFileName = timestamp + "_" + sessionId + "_" + uuid + "." + extension;
            File saveFile = new File(userDir, saveFileName);

            file.transferTo(saveFile);
            return saveFileName;
        } catch (IOException e) {
            // 나중에 custom exception throw
            e.printStackTrace();
        }

        return null;
    }

    private static void addCategoryPath(SelectedCategory selectedCategory, Model model) {
        if (selectedCategory.categoryExists()) {
            model.addAttribute("selectedCategoryPath", selectedCategory.getCategoryPath());
        }
    }

    private void addAllCategories(Model model) {
        List<CategoryDto> allCategories = managementService.getAllCategories();
        model.addAttribute("categories", allCategories);
    }
}

package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.repository.CategoryRepository;
import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubSubcategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubcategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;

    @Value("${app.upload.dir}")
    private String defaultPath;

    @Value("${app.access.url}")
    private String defaultUrl;

    @Cacheable("allCategories")
    public List<CategoryDto> getAllCategories() {
        List<AllCategoryDto> categoriesList = categoryRepository.findAllCategories();
        return convertCategoryDtoList(categoriesList);
    }

    private static List<CategoryDto> convertCategoryDtoList(List<AllCategoryDto> categoriesList) {
        Map<String, CategoryDto> categoryMap = new LinkedHashMap<>();

        for (AllCategoryDto category : categoriesList) {
            String categoryName = category.getCategoryName();
            String subcategoryName = category.getSubCategoryName();
            String subSubcategoryName = category.getSubSubCategoryName();

            CategoryDto categoryDto = categoryMap.computeIfAbsent(categoryName, name -> new CategoryDto(name, new ArrayList<>()));

            SubcategoryDto subcategoryDto = categoryDto.getSubcategories().stream()
                    .filter(sub -> sub.getName().equals(subcategoryName))
                    .findFirst()
                    .orElseGet(() -> {
                        SubcategoryDto newSubcategoryDto = new SubcategoryDto(subcategoryName, new ArrayList<>());
                        categoryDto.getSubcategories().add(newSubcategoryDto);
                        return newSubcategoryDto;
                    });

            subcategoryDto.getSubSubcategories().add(new SubSubcategoryDto(subSubcategoryName));
        }

        return new ArrayList<>(categoryMap.values());
    }

    public void handleImageUpload(MultipartFile imageFile, RegisterInfo registerInfo, String accountId) {
        if (!imageFile.isEmpty()) {
            String uploadDir = getTempStoragePath(defaultPath, accountId);
            String accessDir = getTempStoragePath(defaultUrl, accountId);

            deleteTempImage(uploadDir, registerInfo.getImage().getUploadName());

            registerInfo.setImage(
                    createImage(imageFile, uploadDir, accessDir)
            );
        }
    }

    public void handleImagesUpload(List<MultipartFile> imageFiles, RegisterInfo registerInfo, String accountId) {
        if (!imageFiles.get(0).isEmpty()) {
            String uploadDir = getTempStoragePath(defaultPath, accountId);
            String accessDir = getTempStoragePath(defaultUrl, accountId);

            List<Image> images = registerInfo.getImages();

            if (images != null) {
                registerInfo.getImages().forEach(image -> {
                    deleteTempImage(uploadDir, image.getUploadName());
                });
            }

            registerInfo.setImages(
                    imageFiles.stream()
                            .map(imageFile ->
                                    createImage(imageFile, uploadDir, accessDir)
                            ).toList()
            );
        }
    }

    private String getTempStoragePath(String basePath, String accountId) {
        return storageService.getStoragePath(basePath, accountId, "/tmp");
    }

    private Image createImage(MultipartFile imageFile, String uploadDir, String accessDir) {
        String uploadImageName = uploadImage(imageFile, uploadDir);
        String imageUrl = accessDir + uploadImageName;

        Image image = new Image();
        image.setName(imageFile.getOriginalFilename());
        image.setPath(imageUrl);
        image.setUploadName(uploadImageName);

        return image;
    }

    private String uploadImage(MultipartFile imageFile, String uploadDir) {
        return storageService.upload(imageFile, uploadDir);
    }

    private void deleteTempImage(String imageDir, String imageName) {
        String imagePath = imageDir + imageName;
        storageService.delete(imagePath);
    }
}

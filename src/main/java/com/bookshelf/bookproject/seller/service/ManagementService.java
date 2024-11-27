package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.repository.CategoryRepository;
import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;
import com.bookshelf.bookproject.seller.service.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.upload.dir}")
    private String defaultPath;

    @Value("${app.access.url}")
    private String defaultUrl;

    @Value("${search.api.url}")
    private String searchUrl;

    @Value("${search.api.id}")
    private String clientId;

    @Value("${search.api.secret}")
    private String clientSecret;

    private static int DISPLAY_NUM = 10;


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

    @Cacheable(value = "bookSearch", key = "#bookName + ':' + #page", cacheManager = "cacheManagerWithTTL")
    public String requestBookDataAsJson(String bookName, int page) {
        URI uri = generateBookSearchUriByName(bookName, page);
        return requestBookDataFromNaver(uri);
    }

    @Cacheable(value = "isbnSearch", key = "#isbn", cacheManager = "cacheManagerWithTTL")
    public SearchInfo requestSearchInfo(String isbn) {
        try {
            URI uri = generateBookSearchUriByIsbn(isbn);
            return objectMapper.readValue(requestBookDataFromNaver(uri), SearchInfo.class);
        } catch (JsonProcessingException e) {
            log.warn("json 변환 실패: {}", e.getMessage(), e);
        }
        return new SearchInfo();
    }

    private String requestBookDataFromNaver(URI uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    private URI generateBookSearchUriByIsbn(String isbn) {
        return UriComponentsBuilder
                .fromUriString(searchUrl)
                .path("/book_adv.json")
                .queryParam("d_isbn", isbn)
                .encode()
                .build()
                .toUri();
    }

    private URI generateBookSearchUriByName(String bookName, int page) {
        int start = (page - 1) * DISPLAY_NUM + 1;

        return UriComponentsBuilder
                .fromUriString(searchUrl)
                .path("/book.json")
                .queryParam("query", bookName)
                .queryParam("start", start)
                .queryParam("display", DISPLAY_NUM)
                .encode()
                .build()
                .toUri();
    }

    public static boolean validateBookData(SearchInfo searchInfo) {
        List<BookInfo> items = searchInfo.getItems();
        return items != null && !items.isEmpty();
    }

    public static boolean validateImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        if (!imageFile.isEmpty() && !contentType.startsWith("image/")) {
            log.warn("파일이 이미지 타입이 아닙니다: {}", imageFile.getOriginalFilename());
            return false;
        }
        return true;
    }

    public static boolean validateImageFiles(List<MultipartFile> imageFiles) {
        for (MultipartFile file : imageFiles) {
            return validateImageFile(file);
        }
        return true;
    }
}

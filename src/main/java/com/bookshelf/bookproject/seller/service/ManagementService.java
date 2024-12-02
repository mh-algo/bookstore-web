package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.domain.*;
import com.bookshelf.bookproject.seller.controller.dto.product.RegisterInfo;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Product;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import com.bookshelf.bookproject.seller.repository.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {
    private final SubSubcategoryRepository subsubcategoryRepository;
    private final SellerRepository sellerRepository;
    private final BookProductRepository bookProductRepository;
    private final StorageService storageService;
    private final ManagementCache managementCache;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ImagesRepository imagesRepository;


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

    // 카테고리 데이터 db로부터 가져옴
    @Cacheable("allCategories")
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        List<AllCategoryDto> categoriesList = subsubcategoryRepository.findAllCategories();
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

    public void uploadAndSetAllImages(MultipartFile imageFile,
                                      List<MultipartFile> imageFiles,
                                      RegisterInfo registerInfo,
                                      String accountId) {
        uploadAndSetImage(imageFile, registerInfo, accountId);
        uploadAndSetImages(imageFiles, registerInfo, accountId);
    }

    // 기존 저장되어있던 임시파일을 지운 후, 새로운 임시파일을 저장
    private void uploadAndSetImage(MultipartFile imageFile, RegisterInfo registerInfo, String accountId) {
        if (!imageFile.isEmpty()) {
            String uploadDir = getTempStoragePath(defaultPath, accountId);
            String accessDir = getTempStoragePath(defaultUrl, accountId);

            deleteTmpImage(uploadDir, registerInfo.getImage().getUploadName()); // 기존에 저장된 임시 파일 제거

            // 새로 임시파일을 저장한 후 객체에 해당 정보 set
            registerInfo.setImage(
                    createImage(imageFile, uploadDir, accessDir)
            );
        }
    }

    // 기존 저장되어있던 임시파일을 지운 후, 새로운 임시파일을 저장
    private void uploadAndSetImages(List<MultipartFile> imageFiles, RegisterInfo registerInfo, String accountId) {
        if (!imageFiles.get(0).isEmpty()) {
            String uploadDir = getTempStoragePath(defaultPath, accountId);
            String accessDir = getTempStoragePath(defaultUrl, accountId);

            List<Image> images = registerInfo.getImages();

            if (images != null) {
                registerInfo.getImages().forEach(image ->
                    deleteTmpImage(uploadDir, image.getUploadName()) // 기존에 저장된 임시 파일 제거
                );
            }

            // 새로 임시파일을 저장한 후 객체에 해당 정보 set
            registerInfo.setImages(
                    imageFiles.stream()
                            .map(imageFile ->
                                    createImage(imageFile, uploadDir, accessDir)
                            ).toList()
            );
        }
    }

    // 임시 디렉터리에 저장된 임시 파일을 저장할 공간으로 이동
    private boolean imageSave(String imageName, String accountId) {
        if (imageName != null && !imageName.isEmpty()) {
            return storageService.move(
                    getSavedTempStoragePath(accountId, imageName),
                    getSaveStoragePath(accountId, imageName)
            );
        }
        return false;
    }

    // 임시 디렉터리에 저장된 이미지를 저장할 공간으로 이동
    private boolean imagesSave(List<Image> imageList, String accountId) {
        if (imageList != null) {
            for (Image image : imageList) {
                String uploadName = image.getUploadName();
                boolean isMoved = storageService.move(
                        getSavedTempStoragePath(accountId, uploadName),
                        getSaveStoragePath(accountId, uploadName)
                );
                if (!isMoved) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private String getTempStoragePath(String basePath, String accountId) {
        String path = generateTmpDirectoryPath(basePath, accountId);
        return storageService.getStoragePath(path);
    }

    private String getSavedTempStoragePath(String accountId, String fileName) {
        String path = generateTmpDirectoryPath(defaultPath, accountId);
        return storageService.getFilePath(path, fileName);
    }

    private String getSaveStoragePath(String accountId, String fileName) {
        String path = generateDirectoryPath(defaultPath, accountId);
        return storageService.getFilePath(path, fileName);
    }

    private static String generateDirectoryPath(String basePath, String directoryName) {
        return basePath + directoryName;
    }

    private static String generateTmpDirectoryPath(String basePath, String directoryName) {
        return generateDirectoryPath(basePath, directoryName) + "/tmp";
    }

    private static String generateFilePath(String path, String fileName) {
        return path + fileName;
    }

    // 이미지 파일 저장 후 Image 객체 생성
    private Image createImage(MultipartFile imageFile, String uploadDir, String accessDir) {
        String uploadImageName = uploadImage(imageFile, uploadDir);
        String imageUrl = generateFilePath(accessDir, uploadImageName);

        Image image = new Image();
        image.setName(imageFile.getOriginalFilename());
        image.setPath(imageUrl);
        image.setUploadName(uploadImageName);

        return image;
    }

    // 이미지 업로드
    private String uploadImage(MultipartFile imageFile, String uploadDir) {
        return storageService.upload(imageFile, uploadDir);
    }

    // 임시 파일 제거
    private void deleteTmpImage(String imageDir, String imageName) {
        String imagePath = generateFilePath(imageDir, imageName);
        storageService.delete(imagePath);
    }

    // 캐시에 책 데이터가 없다면 api를 통해 책 데이터를 가져온 후 캐시에 저장
    @Cacheable(value = "bookSearch", key = "#bookName + ':' + #page", cacheManager = "cacheManagerWith10Min")
    public String requestBookDataAsJson(String bookName, int page) {
        URI uri = generateBookSearchUriByName(bookName, page);
        return requestBookDataFromNaver(uri);
    }

    // 캐시에 책 데이터가 없다면 api를 통해 책 데이터를 가져온 후 캐시에 저장
    // isbn을 검색해서 책 데이터를 가져오는데 isbn은 고유값이기 때문에 데이터 1개만 반환됨
    @Cacheable(value = "isbnSearch", key = "#isbn", cacheManager = "cacheManagerWith10Min")
    public SearchInfo requestSearchInfo(String isbn) {
        try {
            URI uri = generateBookSearchUriByIsbn(isbn);
            return objectMapper.readValue(requestBookDataFromNaver(uri), SearchInfo.class);
        } catch (JsonProcessingException e) {
            log.warn("json 변환 실패: {}", e.getMessage(), e);
            return new SearchInfo();
        }
    }

    // api 요청에 대한 응답 반환
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

    // 이미지 파일 검증
    public static boolean isValidImageFile(MultipartFile mainImageFile,
                                              List<MultipartFile> additionalImageFiles) {
        return validateImageFile(mainImageFile) && validateImageFiles(additionalImageFiles) &&
                additionalImageFiles.size() <= 9;
    }

    // 이미지 파일 검증
    private static boolean validateImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        if (!imageFile.isEmpty() && !contentType.startsWith("image/")) {
            log.warn("파일이 이미지 타입이 아닙니다: {}", imageFile.getOriginalFilename());
            return false;
        }
        return true;
    }

    private static boolean validateImageFiles(List<MultipartFile> imageFiles) {
        return imageFiles.stream().allMatch(ManagementService::validateImageFile);
    }

    // 책 데이터 검증
    public static boolean validateBookData(SearchInfo searchInfo) {
        List<BookInfo> items = searchInfo.getItems();
        if (items == null || items.isEmpty()) {
            log.warn("유효하지 않은 데이터입니다");
            return false;
        }
        return true;
    }

    // 입력 데이터 저장
    @Transactional
    public void registerProduct(RegisterInfo registerInfo, List<BookInfo> bookInfo, String accountId) {
        BookProduct bookProduct = createBookProduct(
                registerInfo,
                getSellerByAccountId(accountId),
                getBook(bookInfo.get(0)),
                getSubSubcategory(registerInfo.getSelectedCategory())
        );
        bookProductRepository.save(bookProduct);
        saveImages(registerInfo.getImages(), bookProduct);
    }

    private static BookProduct createBookProduct(RegisterInfo registerInfo, Seller sellerByAccountId,
                                                 Book book, SubSubcategory subSubcategory) {
        Product product = registerInfo.getProduct();
        Image image = registerInfo.getImage();

        return BookProduct.builder()
                .seller(sellerByAccountId)
                .book(book)
                .subSubcategory(subSubcategory)
                .price(product.getPrice())
                .discount(product.getDiscount())
                .inventory(product.getAmount())
                .deliveryFee(registerInfo.getDeliveryFee())
                .mainImageUrl(image.getPath())
                .build();
    }

    private SubSubcategory getSubSubcategory(SelectedCategory selectedCategory) {
        return subsubcategoryRepository.findCategoryGroupByName(
                selectedCategory.getCategoryName(),
                selectedCategory.getSubcategoryName(),
                selectedCategory.getSubSubcategoryName()
        );
    }

    private Book getBook(BookInfo bookInfo) {
        return managementCache.findOrSaveBook(bookInfo);
    }

    private Seller getSellerByAccountId(String accountId) {
        return sellerRepository.findByAccountId(accountId);
    }

    private void saveImages(List<Image> imageList, BookProduct bookProduct) {
        if (imageList != null) {
            for (Image image : imageList) {
                Images images = createImages(bookProduct, image.getPath());
                imagesRepository.save(images);
            }
        }
    }

    private static Images createImages(BookProduct bookProduct, String imagePath) {
        return new Images(bookProduct, imagePath);
    }

    // 이미지 파일 저장
    public boolean saveAllImageFiles(RegisterInfo registerInfo, String accountId) {
        boolean isImageSaved = imageSave(registerInfo.getImage().getUploadName(), accountId);
        boolean isImagesSaved = imagesSave(registerInfo.getImages(), accountId);

        if (isImageSaved && isImagesSaved) {
            return deleteFilesInTmpDirectory(accountId);
        }
        return false;
    }

    // 임시 디렉터리 파일 삭제
    private boolean deleteFilesInTmpDirectory(String accountId) {
        return storageService.deleteFilesInDirectory(
                generateTmpDirectoryPath(defaultPath, accountId));
    }
}

package com.bookshelf.bookproject.common;

import com.bookshelf.bookproject.common.repository.SubSubcategoryRepository;
import com.bookshelf.bookproject.common.repository.dto.AllCategoryDto;
import com.bookshelf.bookproject.common.repository.dto.Category;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubSubcategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubcategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final SubSubcategoryRepository subSubCategoryRepository;

    // 카테고리 데이터 db로부터 가져옴
    @Cacheable("allCategories")
    @Transactional(readOnly = true)
    public Map<Long, CategoryDto> getAllCategories() {
        List<AllCategoryDto> categoriesList = subSubCategoryRepository.findAllCategories();
        return convertCategoryDtoList(categoriesList);
    }

    private static Map<Long, CategoryDto> convertCategoryDtoList(List<AllCategoryDto> categoriesList) {
        Map<Long, CategoryDto> categoryMap = new LinkedHashMap<>();

        for (AllCategoryDto category : categoriesList) {
            Long categoryId = category.getCategoryId();
            String categoryName = category.getCategoryName();
            Long subCategoryId = category.getSubcategoryId();
            String subcategoryName = category.getSubcategoryName();
            Long subSubCategoryId = category.getSubSubcategoryId();
            String subSubcategoryName = category.getSubSubcategoryName();

            CategoryDto categoryDto = categoryMap.computeIfAbsent(categoryId, id -> new CategoryDto(categoryName, new LinkedHashMap<>()));

            SubcategoryDto subcategoryDto = categoryDto.getSubcategories().entrySet().stream()
                    .filter(sub -> Objects.equals(sub.getKey(), subCategoryId))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseGet(() -> {
                        SubcategoryDto newSubcategoryDto = new SubcategoryDto(subcategoryName, new LinkedHashMap<>());
                        categoryDto.getSubcategories().put(subCategoryId, newSubcategoryDto);
                        return newSubcategoryDto;
                    });

            subcategoryDto.getSubSubcategories().put(subSubCategoryId, new SubSubcategoryDto(subSubcategoryName));
        }

        return categoryMap;
    }

    public static boolean validateCategory(Map<Long, CategoryDto> categoryMap, Category category) {
        Long categoryId = category.getCategoryId();

        if (categoryMap.containsKey(categoryId)) {
            Map<Long, SubcategoryDto> subcategories = categoryMap.get(categoryId).getSubcategories();
            Long subcategoryId = category.getSubcategoryId();

            if (subcategories.containsKey(subcategoryId)) {
                Map<Long, SubSubcategoryDto> subSubcategories = subcategories.get(subcategoryId).getSubSubcategories();
                Long subSubcategoryId = category.getSubSubcategoryId();

                return subSubcategories.containsKey(subSubcategoryId);
            }
        }
        return false;
    }
}

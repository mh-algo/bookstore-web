package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.seller.repository.CategoryRepository;
import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubSubcategoryDto;
import com.bookshelf.bookproject.seller.service.dto.SubcategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final CategoryRepository categoryRepository;

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
}

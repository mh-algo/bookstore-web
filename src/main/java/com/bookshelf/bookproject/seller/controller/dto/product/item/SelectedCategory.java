package com.bookshelf.bookproject.seller.controller.dto.product.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SelectedCategory {
    @NotBlank
    private String categoryName;

    @NotBlank
    private String subcategoryName;

    @NotBlank
    private String subSubcategoryName;

    public String getCategoryPath() {
        return categoryName + " > " + subcategoryName + " > " + subSubcategoryName;
    }

    public boolean categoryExists() {
        return !categoryName.isEmpty() && !subcategoryName.isEmpty() && !subSubcategoryName.isEmpty();
    }
}

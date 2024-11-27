package com.bookshelf.bookproject.seller.controller.dto.product;

import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.controller.dto.product.item.Product;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class RegisterInfo {
    @Valid
    private SelectedCategory selectedCategory;

    @Valid
    private Product product;

    private Image image;
    private List<Image> images;
    private String imagesMessage;
}

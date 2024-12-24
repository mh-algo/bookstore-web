package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookProduct extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name="fk_book_product_seller"))
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, foreignKey = @ForeignKey(name="fk_book_product_book"))
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_subcategory_id", nullable = false, foreignKey = @ForeignKey(name="fk_book_product_sub_subcategory"))
    private SubSubcategory subSubcategory;

    @Column(columnDefinition = "int constraint chk_price check(price >= 0)")
    private Integer price;

    @Column(nullable = false, columnDefinition = "int default 0 constraint chk_discount check(discount >= 0)")
    private Integer discount;

    @Column(nullable = false, columnDefinition = "int default 0 constraint chk_stock check(stock >= 0)")
    private Integer stock;

    @Column(name = "delivery_fee", nullable = false, columnDefinition = "int default 0 constraint chk_delivery_fee check(delivery_fee >= 0)")
    private Integer deliveryFee;

    @Column(name = "main_image_url")
    private String mainImageUrl;

    @OneToMany(mappedBy = "bookProduct")
    private final List<Images> images = new ArrayList<>();

    @Builder
    public BookProduct(Seller seller, Book book, SubSubcategory subSubcategory, Integer price, Integer discount, Integer stock, Integer deliveryFee, String mainImageUrl) {
        this.seller = seller;
        this.book = book;
        this.subSubcategory = subSubcategory;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.deliveryFee = deliveryFee;
        this.mainImageUrl = mainImageUrl;
    }

    public void addImages(Images image) {
        this.images.add(image);
    }

    public static BookProduct empty() {
        return BookProduct.builder().build();
    }
}

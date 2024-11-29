package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name="fk_book_product_seller"))
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, foreignKey = @ForeignKey(name="fk_book_product_book"))
    private Book book;

    @Column(columnDefinition = "int constraint chk_book_product_price check(price >= 0)")
    private Integer price;

    @Column(nullable = false, columnDefinition = "int default 0 constraint chk_discount check(discount >= 0)")
    private Integer discount;

    @Column(nullable = false, columnDefinition = "int default 0 constraint chk_inventory check(inventory >= 0)")
    private Integer inventory;

    @Column(name = "delivery_fee", nullable = false, columnDefinition = "int default 0 constraint chk_delivery_fee check(delivery_fee >= 0)")
    private Integer deliveryFee;

    @Column(name = "main_image_url")
    private String mainImageUrl;
}

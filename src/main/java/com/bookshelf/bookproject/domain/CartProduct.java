package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_cart_id_book_product_id", columnNames = {"cart_id", "book_product_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartProduct extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cart_product_cart"))
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cart_product_book_product"))
    private BookProduct bookProduct;

    @Column(nullable = false, columnDefinition = "int default 1 constraint chk_quantity check(quantity >= 1)")
    private Integer quantity;

    public CartProduct(Cart cart, BookProduct bookProduct, Integer quantity) {
        this.cart = cart;
        this.bookProduct = bookProduct;
        this.quantity = quantity;
    }

    public boolean isEmpty() {
        return id == null;
    }
}

package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_isbn", columnNames = "isbn"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtitle;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(length = 20, nullable = false)
    private String author;

    @Column(length = 20, nullable = false)
    private String publisher;

    @Column(length = 20, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false, columnDefinition = "int constraint chk_book_price check(price >= 0)")
    private Integer price;

    @Column(name = "book_description", length = 2500, nullable = false)
    private String description;

    @Builder
    public Book(String title, String subtitle, String imageUrl, String author, String publisher, String isbn, LocalDate publishedDate, Integer price, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.price = price;
        this.description = description;
    }

    public boolean isEmpty() {
        return id == null;
    }
}

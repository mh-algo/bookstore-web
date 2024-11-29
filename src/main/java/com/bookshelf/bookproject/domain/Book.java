package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_isbn", columnNames = "isbn"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

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

    @Column(name = "book_description", length = 1000, nullable = false)
    private String description;
}

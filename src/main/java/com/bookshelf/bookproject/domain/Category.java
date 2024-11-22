package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_category_name", columnNames = "category_name")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", length = 20, nullable = false, insertable = false, updatable = false)
    private String name;
}

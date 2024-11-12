package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_bank_name", columnNames = "bank_name")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="bank_name", length = 20, nullable = false, insertable = false, updatable = false)
    private String name;
}

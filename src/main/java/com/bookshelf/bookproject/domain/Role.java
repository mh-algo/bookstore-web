package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles", uniqueConstraints =
        @UniqueConstraint(name = "unique_role_type", columnNames = "role_type")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 권한을 나타냄
     */
    @Column(name = "role_type", length = 20, nullable = false, insertable = false, updatable = false)
    private String type;
}

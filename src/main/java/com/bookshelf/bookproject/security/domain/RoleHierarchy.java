package com.bookshelf.bookproject.security.domain;

import com.bookshelf.bookproject.domain.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_roles_id_parent_id", columnNames = {"roles_id", "parent_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoleHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_authority_roles"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_authority_parent_authority"))
    private RoleHierarchy parent;
}

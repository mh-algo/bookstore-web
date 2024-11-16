package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_management", uniqueConstraints =
        @UniqueConstraint(name = "unique_roles_id_accounts_id", columnNames = {"roles_id", "accounts_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoleManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false, foreignKey = @ForeignKey(name="fk_role_management_roles"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounts_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_role_management_accounts"))
    private Account account;

    public RoleManagement(Role role, Account account) {
        this.role = role;
        this.account = account;
    }
}

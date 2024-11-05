package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts", uniqueConstraints =
        @UniqueConstraint(name = "unique_account_id", columnNames = "account_id")
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(length = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Account extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", length = 20, nullable = false)
    private String name;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "pwd", length = 60, nullable = false)
    private String password;

    @OneToMany(mappedBy = "account")
    private final List<RoleManagement> roleManagements = new ArrayList<>();

    @Column(name = "account_status", columnDefinition = "varchar(10) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Account(String name, String accountId, String password, AccountStatus status) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.status = status;
    }
}

package com.bookshelf.bookproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.entity.QAccount.account;
import static com.bookshelf.bookproject.domain.entity.QRole.role;
import static com.bookshelf.bookproject.domain.entity.QRoleManagement.roleManagement;

@RequiredArgsConstructor
public class CustomAccountRepositoryImpl implements CustomAccountRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findRolesByAccountId(String accountId) {
        return queryFactory
                .select(role.type)
                .from(account)
                .join(account.roleManagements, roleManagement)
                .on(account.accountId.eq(accountId))
                .join(roleManagement.role, role)
                .fetch();
    }
}

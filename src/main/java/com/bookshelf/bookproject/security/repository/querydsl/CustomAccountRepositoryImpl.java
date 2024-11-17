package com.bookshelf.bookproject.security.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QAccount.account;
import static com.bookshelf.bookproject.domain.QRole.role;
import static com.bookshelf.bookproject.domain.QRoleManagement.roleManagement;

@RequiredArgsConstructor
public class CustomAccountRepositoryImpl implements CustomAccountRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 주어진 사용자 아이디에 해당하는 권한 리스트를 반환
     *
     * @param accountId 사용자 아이디
     * @return 주어진 사용자 아이디에 해당하는 권한 리스트
     */
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

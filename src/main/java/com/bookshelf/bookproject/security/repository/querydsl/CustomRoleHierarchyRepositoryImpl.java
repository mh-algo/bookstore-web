package com.bookshelf.bookproject.security.repository.querydsl;

import com.bookshelf.bookproject.domain.QRole;
import com.bookshelf.bookproject.domain.QRoleHierarchy;
import com.bookshelf.bookproject.security.repository.dto.RoleHierarchyDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QRole.role;
import static com.bookshelf.bookproject.domain.QRoleHierarchy.roleHierarchy;

@RequiredArgsConstructor
public class CustomRoleHierarchyRepositoryImpl implements CustomRoleHierarchyRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoleHierarchyDto> findAllByParentNotNull() {
        QRoleHierarchy parent = new QRoleHierarchy("parent");
        QRole parentRole = new QRole("parentRole");

        return queryFactory
                .select(Projections.constructor(RoleHierarchyDto.class, role.type, parentRole.type))
                .from(roleHierarchy)
                .join(roleHierarchy.role, role)
                .join(roleHierarchy.parent, parent)
                .join(parent.role, parentRole)
                .fetch();
    }
}

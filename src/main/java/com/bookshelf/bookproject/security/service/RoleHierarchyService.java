package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.repository.RoleHierarchyRepository;
import com.bookshelf.bookproject.repository.dto.RoleHierarchyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleHierarchyService {
    private final RoleHierarchyRepository roleHierarchyRepository;

    public RoleHierarchyService(RoleHierarchyRepository roleHierarchyRepository) {
        this.roleHierarchyRepository = roleHierarchyRepository;
    }

    /**
     * 모든 권한 계층을 문자열 형태로 변환하여 반환
     * <p> 이 메서드는 권한 계층 정보가 담긴 {@link RoleHierarchyDto} 객체 리스트를 조회한 후,
     * 각 권한 간의 관계를 문자열로 변환하여 반환합니다. 반환된 문자열은 각 계층 정보를 ">" 기호로 연결하여
     * 계층 구조를 표현하며, 각 관계는 줄바꿈으로 구분됩니다.
     *
     * @return 권한 계층을 나타내는 {@link String} 타입의 문자열
     */
    @Transactional
    public String findAllHierarchy() {
        List<RoleHierarchyDto> rolesHierarchy = roleHierarchyRepository.findAllByParentNotNull();

        StringBuilder hierarchyRole = new StringBuilder();
        rolesHierarchy.forEach(roleHierarchy -> {
            hierarchyRole.append(roleHierarchy.getParent());
            hierarchyRole.append(" > ");
            hierarchyRole.append(roleHierarchy.getRole());
            hierarchyRole.append("\n");
        });
        return hierarchyRole.toString();
    }
}

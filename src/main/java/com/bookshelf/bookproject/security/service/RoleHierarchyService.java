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

package com.bookshelf.bookproject.security.mapper;

import com.bookshelf.bookproject.repository.AuthorityManagementRepository;
import com.bookshelf.bookproject.repository.dto.AuthorityDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathRoleMapper {
    private final AuthorityManagementRepository authorityManagementRepository;

    public PathRoleMapper(AuthorityManagementRepository authorityManagementRepository) {
        this.authorityManagementRepository = authorityManagementRepository;
    }

    public Map<String, String> getPathRoleMappings() {
        List<AuthorityDto> authorityList = authorityManagementRepository.findAuthorityAll();

        return authorityList.stream()
                .collect(Collectors.toMap(AuthorityDto::getPath, AuthorityDto::getRole));
    }
}

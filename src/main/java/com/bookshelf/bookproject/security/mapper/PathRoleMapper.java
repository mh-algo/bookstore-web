package com.bookshelf.bookproject.security.mapper;

import com.bookshelf.bookproject.security.repository.AuthorityManagementRepository;
import com.bookshelf.bookproject.security.repository.dto.AuthorityDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathRoleMapper {
    private final AuthorityManagementRepository authorityManagementRepository;

    public PathRoleMapper(AuthorityManagementRepository authorityManagementRepository) {
        this.authorityManagementRepository = authorityManagementRepository;
    }

    /**
     * 모든 {@link AuthorityDto} 객체에서 경로와 권한 정보를 조회하여 Map으로 반환합니다.
     * <p> {@link AuthorityDto} 리스트에서 각 객체의 경로(path)와 권한(role)을 추출하여,
     * 경로를 key, 권한을 value로 갖는 {@code Map<String, String>} 형태로 반환합니다.
     *
     * @return 각 경로(path)를 key로, 해당 권한(role)을 value로 갖는 {@link Map} 객체
     */
    public Map<String, String> getPathRoleMappings() {
        List<AuthorityDto> authorityList = authorityManagementRepository.findAuthorityAll();

        return authorityList.stream()
                .collect(Collectors.toUnmodifiableMap(AuthorityDto::getPath, AuthorityDto::getRole));
    }
}

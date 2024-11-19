package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.repository.AccessDeniedPolicyRepository;
import com.bookshelf.bookproject.security.repository.dto.AccessDeniedPolicyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessDeniedPolicyCache {
    private final AccessDeniedPolicyRepository accessDeniedPolicyRepository;

    @Cacheable("pathPolicy")
    @Transactional(readOnly = true)
    public List<AccessDeniedPolicyDto> getCachedAccessDeniedPolicies() {
        return accessDeniedPolicyRepository.findAllAccessDeniedPolicy();
    }
}

package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.repository.dto.AccessDeniedPolicyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessDeniedPolicyService {
    private final AccessDeniedPolicyCache accessDeniedPolicyCache;
    private final PathMatcher pathMatcher;

    public Optional<String> findRedirectByRequest(String requestPath) {
        return accessDeniedPolicyCache.getCachedAccessDeniedPolicies().stream()
                .filter(policy -> pathMatcher.match(policy.getRequestPath(), requestPath))
                .map(AccessDeniedPolicyDto::getRedirectPath)
                .findFirst();
    }
}

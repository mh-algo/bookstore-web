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

    /**
     * 주어진 요청 경로에 해당하는 리다이렉트 경로를 조회하여 반환
     * <p> 이 메서드는 접근이 거부된 요청 경로({@code requestPath})에 해당하는 리다이렉트 경로({@code redirectPath})를 조회합니다.
     * 경로가 일치하는 정책이 존재할 경우, 해당 리다이렉트 경로를 반환하며, 일치하는 정책이 없는 경우에는 {@link Optional#empty()}를 반환합니다.
     *
     * @param requestPath 접근이 거부된 요청 경로
     * @return 접근이 거부된 요청 경로에 해당하는 리다이렉트 경로를 담은 {@link Optional} 객체.
     *         해당 경로에 대한 정책이 없으면 {@link Optional#empty()}를 반환합니다.
     */
    public Optional<String> findRedirectByRequest(String requestPath) {
        return accessDeniedPolicyCache.getCachedAccessDeniedPolicies().stream()
                .filter(policy -> pathMatcher.match(policy.getRequestPath(), requestPath))
                .map(AccessDeniedPolicyDto::getRedirectPath)
                .findFirst();
    }
}

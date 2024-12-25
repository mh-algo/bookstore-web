package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.repository.AccessDeniedPolicyRepository;
import com.bookshelf.bookproject.security.repository.dto.AccessDeniedPolicyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;

@Service
@RequiredArgsConstructor
public class AccessDeniedPolicyCache {
    private final AccessDeniedPolicyRepository accessDeniedPolicyRepository;

    /**
     * 접근 거부 정책을 조회하여 캐시된 결과를 반환
     * <p> 이 메서드는 접근이 거부된 요청 경로({@code requestPath})와 해당 경로에 대한 리다이렉트 경로({@code redirectPath})를 포함한
     * {@link AccessDeniedPolicyDto} 객체의 리스트를 조회합니다.
     * <p> 최초 실행 이후 반환된 결과는 캐시에 저장되며, 이후 호출 시에는 캐시된 값을 반환하여 데이터베이스 접근을 줄이고 성능을 최적화합니다.
     *
     * @return 각 접근 거부 경로({@code requestPath})와 리다이렉트 경로({@code redirectPath}) 정보를 담은
     * {@link List}<{@link AccessDeniedPolicyDto}> 객체
     */
    @Cacheable(value = "pathPolicy", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public List<AccessDeniedPolicyDto> getCachedAccessDeniedPolicies() {
        return accessDeniedPolicyRepository.findAllAccessDeniedPolicy();
    }
}

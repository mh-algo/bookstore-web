package com.bookshelf.bookproject.security.manager;

import com.bookshelf.bookproject.repository.AuthorityManagementRepository;
import com.bookshelf.bookproject.security.mapper.PathRoleMapper;
import com.bookshelf.bookproject.security.service.DynamicAuthorizationService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.function.Supplier;

public class CustomDynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final AuthorizationDecision ACCESS = new AuthorizationDecision(true);
    private List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;
    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private final AuthorityManagementRepository authorityManagementRepository;

    public CustomDynamicAuthorizationManager(HandlerMappingIntrospector handlerMappingIntrospector, AuthorityManagementRepository authorityManagementRepository) {
        this.handlerMappingIntrospector = handlerMappingIntrospector;
        this.authorityManagementRepository = authorityManagementRepository;
    }

    /**
     * 경로와 권한 매핑 정보를 사용하여 {@code mappings} 리스트를 초기화합니다.
     * <p> 이 메서드는 애플리케이션 시작 시 {@link PostConstruct} 어노테이션에 의해 자동으로 호출되며,
     * {@link AuthorityManagementRepository}로부터 권한 매핑 정보를 가져와 동적 인가 매핑을 설정합니다.
     * 각 경로(path)와 권한(role)을 매핑하여 {@link RequestMatcherEntry} 리스트를 생성하고,
     * 이를 {@code mappings} 필드에 할당합니다.
     */
    @PostConstruct
    private void initialize() {
        DynamicAuthorizationService dynamicAuthorizationService = new DynamicAuthorizationService(new PathRoleMapper(authorityManagementRepository));

        this.mappings = dynamicAuthorizationService.getPathRoleMappings().entrySet().stream()
                .map(entry -> new RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>(
                        new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()),
                        AuthorityAuthorizationManager.hasAuthority(entry.getValue()))
                ).toList();
    }

    /**
     * 요청 경로와 사용자 권한을 기반으로 인가 여부를 결정
     * <p> 요청된 경로를 {@code mappings}에 있는 경로들과 비교하여 일치하는 매핑을 찾습니다.
     * 경로가 일치할 경우, 해당 매핑의 권한과 {@code authentication}의 권한을 비교하여 인가 여부를 결정합니다.
     * 경로가 일치하지 않거나 모든 매핑 조건이 만족하지 않을 경우, 기본적으로 인가를 허용합니다.
     *
     * @param authentication 사용자의 인증 정보를 제공하는 {@link Supplier} 객체
     * @param request 권한을 확인할 대상 요청을 나타내는 {@link RequestAuthorizationContext} 객체
     * @return 요청이 허용되면 {@code AuthorizationDecision(true)}, 거부되면 {@code AuthorizationDecision(false)}
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext request) {
        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : mappings) {
            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request.getRequest());

            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                return manager.check(authentication,
                        new RequestAuthorizationContext(request.getRequest(), matchResult.getVariables()));
            }
        }

        return ACCESS;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }
}

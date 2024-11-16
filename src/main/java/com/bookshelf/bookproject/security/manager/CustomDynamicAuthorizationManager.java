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

    @PostConstruct
    private void initialize() {
        DynamicAuthorizationService dynamicAuthorizationService = new DynamicAuthorizationService(new PathRoleMapper(authorityManagementRepository));

        this.mappings = dynamicAuthorizationService.getPathRoleMappings().entrySet().stream()
                .map(entry -> new RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>(
                        new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()),
                        AuthorityAuthorizationManager.hasAuthority(entry.getValue()))
                ).toList();
    }

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

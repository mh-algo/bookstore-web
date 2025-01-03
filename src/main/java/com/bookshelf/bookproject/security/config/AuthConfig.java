package com.bookshelf.bookproject.security.config;

import com.bookshelf.bookproject.security.handler.CustomAccessDeniedHandler;
import com.bookshelf.bookproject.security.repository.AuthorityManagementRepository;
import com.bookshelf.bookproject.security.handler.FormAuthenticationFailureHandler;
import com.bookshelf.bookproject.security.manager.CustomDynamicAuthorizationManager;
import com.bookshelf.bookproject.security.provider.FormLoginProvider;
import com.bookshelf.bookproject.security.service.AccessDeniedPolicyService;
import com.bookshelf.bookproject.security.service.FormLoginCache;
import com.bookshelf.bookproject.security.service.FormLoginService;
import com.bookshelf.bookproject.security.service.RoleHierarchyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class AuthConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(FormLoginCache formLoginCache) {
        return new FormLoginService(formLoginCache);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new FormLoginProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FormAuthenticationFailureHandler();
    }

    @Bean
    public CustomDynamicAuthorizationManager customDynamicAuthorizationManager(
            HandlerMappingIntrospector handlerMappingIntrospector,
            AuthorityManagementRepository authorityManagementRepository,
            RoleHierarchyImpl roleHierarchy) {
        return new CustomDynamicAuthorizationManager(handlerMappingIntrospector, authorityManagementRepository, roleHierarchy);
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy(RoleHierarchyService roleHierarchyService) {
        String allHierarchy = roleHierarchyService.findAllHierarchy();
        return RoleHierarchyImpl.fromHierarchy(allHierarchy);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(AccessDeniedPolicyService accessDeniedPolicyService) {
        return new CustomAccessDeniedHandler(accessDeniedPolicyService);
    }

    @Bean
    public PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }
}

package com.bookshelf.bookproject.security.config;

import com.bookshelf.bookproject.repository.AccountRepository;
import com.bookshelf.bookproject.security.handler.FormAuthenticationFailureHandler;
import com.bookshelf.bookproject.security.provider.FormLoginProvider;
import com.bookshelf.bookproject.security.service.FormLoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class AuthConfig {
    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new FormLoginService(accountRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        return new FormLoginProvider(userDetailsService);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FormAuthenticationFailureHandler();
    }
}

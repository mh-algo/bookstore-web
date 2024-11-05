package com.bookshelf.bookproject.security.handler;

import com.bookshelf.bookproject.security.exception.AccountDeletedException;
import com.bookshelf.bookproject.security.exception.PasswordBlankException;
import com.bookshelf.bookproject.security.exception.UsernameBlankException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String error = exception.toString();

        if  (exception instanceof UsernameBlankException) {
            error = "UsernameBlankException";
        } else if(exception instanceof PasswordBlankException) {
            error = "PasswordBlankException";
        } else if (exception instanceof BadCredentialsException) {
            error = "BadCredentialsException";
        } else if (exception instanceof UsernameNotFoundException) {
            error = "UsernameNotFoundException";
        } else if (exception instanceof DisabledException) {
            error = "DisabledException";
        } else if (exception instanceof AccountDeletedException) {
            error = "AccountDeletedException";
        }

        logger.debug(exception.getMessage());
        request.getSession().setAttribute("error", error);
        setDefaultFailureUrl("/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}

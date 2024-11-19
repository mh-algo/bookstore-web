package com.bookshelf.bookproject.security.handler;

import com.bookshelf.bookproject.security.exception.RedirectException;
import com.bookshelf.bookproject.security.service.AccessDeniedPolicyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.io.IOException;

public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {
    private final AccessDeniedPolicyService accessDeniedPolicyService;

    public CustomAccessDeniedHandler(AccessDeniedPolicyService accessDeniedPolicyService) {
        this.accessDeniedPolicyService = accessDeniedPolicyService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String requestPath = request.getRequestURI();

        accessDeniedPolicyService.findRedirectByRequest(requestPath).ifPresent(redirectPath -> {
                    try {
                        response.sendRedirect(redirectPath);
                    } catch (IOException e) {
                        throw new RedirectException("Redirect failed", e);
                    }
                }
        );
        super.handle(request, response, accessDeniedException);
    }
}

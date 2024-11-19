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

    /**
     * 접근 거부 상황을 처리하고, 필요한 경우 사용자에게 리다이렉트 응답을 보냄
     * <p> 이 메서드는 접근이 거부된 요청 경로를 기반으로 설정된 리다이렉트 경로를 조회하여,
     * 해당 경로로 사용자를 리다이렉트합니다. 리다이렉트가 실패할 경우 {@link RedirectException}을 던집니다.
     * 리다이렉트 경로가 없을 경우, 부모 클래스의 기본 접근 거부 처리를 수행합니다.
     *
     * @param request 접근이 거부된 요청을 나타내는 {@link HttpServletRequest} 객체
     * @param response 사용자 에이전트에 실패 사실을 알리기 위한 {@link HttpServletResponse} 객체
     * @param accessDeniedException 접근 거부를 일으킨 예외
     * @throws IOException 리다이렉트 중 I/O 오류가 발생한 경우 발생
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우 발생
     */
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

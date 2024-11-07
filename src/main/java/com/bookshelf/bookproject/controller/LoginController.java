package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.controller.dto.FormLogin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {
    /**
     * 로그인 요청 처리하고, 유효성 검사 수행
     * <p> 사용자가 입력한 로그인 정보를 받아 유효성 검사를 진행합니다.
     * 세션에 저장된 error가 존재하는 경우, 유효성 검사 실패로 간주하여
     * bindingResult에 에러를 담고 로그인 페이지를 다시 반환합니다.
     *
     * @param request 요청 시 들어온 HTTP 요청 정보를 담고 있는 {@link HttpServletRequest} 객체
     * @param formLogin 뷰에서 에러 처리를 위한 빈 FormLogin 객체
     * @param bindingResult 유효성 검사 결과를 담을 {@link BindingResult} 객체
     * @return 로그인 페이지
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute FormLogin formLogin,
                        BindingResult bindingResult) {
        try {
            HttpSession session = request.getSession(false);
            String error = (String) session.getAttribute("error");
            switch (error) {
                case "UsernameBlankException" -> bindingResult.rejectValue("username", error);
                case "PasswordBlankException" -> bindingResult.rejectValue("password", error);
                case "BadCredentialsException", "UsernameNotFoundException", "DisabledException",
                     "AccountDeletedException" -> bindingResult.reject(error);
            }
            session.removeAttribute("error");
        } catch (NullPointerException ignored) {}

        return "login";
    }
}



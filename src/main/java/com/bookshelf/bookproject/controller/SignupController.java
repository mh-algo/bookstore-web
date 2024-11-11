package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.controller.dto.SignupUser;
import com.bookshelf.bookproject.controller.dto.Username;
import com.bookshelf.bookproject.controller.enums.EnumMapper;
import com.bookshelf.bookproject.controller.enums.EnumMapperValue;
import com.bookshelf.bookproject.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final EnumMapper enumMapper;

    private static final String EMAIL_ADDRESS_TYPE = "emailAddressType";
    private static final String PHONE_PREFIX_TYPE = "phonePrefixType";
    private final SignupService signupService;

    /**
     * 이메일 주소에 대한 Enum 값을 모델에 추가
     * <p> 이메일 주소의 목록을 가져와 모델에 추가합니다.
     *
     * @return 이메일 주소 종류에 해당하는 {@link EnumMapperValue} 리스트
     */
    @ModelAttribute(EMAIL_ADDRESS_TYPE)
    public List<EnumMapperValue> emailAddressType() {
        return enumMapper.get(EMAIL_ADDRESS_TYPE);
    }

    /**
     * 휴대전화 앞번호 종류에 대한 Enum 값을 모델에 추가
     * <p> 휴대전화 앞번호의 목록을 가져와 모델에 추가합니다.
     *
     * @return 휴대전화 앞번호에 해당하는 {@link EnumMapperValue} 리스트
     */
    @ModelAttribute(PHONE_PREFIX_TYPE)
    public List<EnumMapperValue> phonePrefixType() {
        return enumMapper.get(PHONE_PREFIX_TYPE);
    }

    /**
     * 회원가입 페이지에 대한 GET 요청 처리
     * <p> 새로운 사용자 등록을 위한 빈 {@link SignupUser} 객체를 모델에 추가합니다.
     *
     * @param model 뷰에 전달할 데이터를 추가하기 위한 {@link Model} 객체
     * @return 회원가입 페이지
     */
    @GetMapping("/user")
    public String signupUser(Model model) {
        model.addAttribute("signupUser", new SignupUser());
        return "user/signup";
    }

    /**
     * 회원가입 페이지에 대한 Post 요청을 처리하고, 유효성 검사 수행
     * <p> 사용자가 입력한 회원가입 데이터를 받아 유효성 검사를 진행합니다.
     * <p> 문제가 있는 경우 회원가입 페이지로 다시 반환하고,
     * 문제가 없는 경우 {@link SignupUser} 객체를 저장한 후
     * 메인 페이지로 리다이렉트합니다.
     *
     * @param signupUser 입력받은 회원 정보
     * @param bindingResult 유효성 검사 결과를 담고 있는 {@link BindingResult} 객체
     * @return 유효성 검사가 실패하면 회원가입 페이지, 성공하면 메인 페이지로 리다이렉트
     */
    @PostMapping("/user")
    public String saveAccount(@Valid @ModelAttribute SignupUser signupUser, BindingResult bindingResult) {
        System.out.println("signupUser = " + signupUser);
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        signupService.saveUserAccount(signupUser);

        return "redirect:/";
    }

    /**
     * 아이디 사용 가능 여부 확인
     * <p> 아이디 검증에 통과하면 200 OK 상태 코드를 전달하고,
     * 실패하면 400 Bad Request 상태 코드와 함께 에러 메시지를 반환합니다.
     *
     * @param username 검증할 {@code username} 필드를 담고 있는 {@link Username} 객체
     * @param bindingResult 유효성 검사 결과를 담고 있는 {@link BindingResult} 객체
     * @return 검증 통과 시 200 OK, 실패 시 400 Bad Request와 에러 메시지를 담은 {@code ResponseEntity}
     */
    @PostMapping("/user/check-username")
    public ResponseEntity<Map<String, String>> checkUsername(@Valid @RequestBody Username username, BindingResult bindingResult) {
        Map<String, String> response = new LinkedHashMap<>();

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError("username")).getDefaultMessage();
            response.put("error", errorMessage);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}

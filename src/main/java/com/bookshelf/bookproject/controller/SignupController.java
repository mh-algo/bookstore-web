package com.bookshelf.bookproject.controller;

import com.bookshelf.bookproject.controller.dto.Signup;
import com.bookshelf.bookproject.controller.dto.SignupSeller;
import com.bookshelf.bookproject.controller.dto.Username;
import com.bookshelf.bookproject.controller.enums.EnumMapper;
import com.bookshelf.bookproject.controller.enums.EnumMapperValue;
import com.bookshelf.bookproject.service.SignupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

import static com.bookshelf.bookproject.controller.enums.EnumKeys.*;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final SignupService signupService;
    private final List<EnumMapperValue> emailAddressList;
    private final List<EnumMapperValue> phonePrefixList;
    private final List<EnumMapperValue> localNumberList;

    public SignupController(EnumMapper enumMapper, SignupService signupService) {
        this.signupService = signupService;
        this.emailAddressList = enumMapper.get(EMAIL_ADDRESS_TYPE);
        this.phonePrefixList = enumMapper.get(PHONE_PREFIX_TYPE);
        this.localNumberList = enumMapper.get(LOCAL_NUMBER_TYPE);
    }

    /**
     * 이메일 주소에 대한 Enum 값을 모델에 추가
     * <p> 이메일 주소의 목록을 가져와 모델에 추가합니다.
     *
     * @return 이메일 주소 종류에 해당하는 {@link EnumMapperValue} 리스트
     */
    @ModelAttribute(EMAIL_ADDRESS_TYPE)
    public List<EnumMapperValue> emailAddressType() {
        return emailAddressList;
    }

    /**
     * 휴대전화 앞번호 종류에 대한 Enum 값을 모델에 추가
     * <p> 휴대전화 앞번호의 목록을 가져와 모델에 추가합니다.
     *
     * @return 휴대전화 앞번호에 해당하는 {@link EnumMapperValue} 리스트
     */
    @ModelAttribute(PHONE_PREFIX_TYPE)
    public List<EnumMapperValue> phonePrefixType() {
        return phonePrefixList;
    }

    @GetMapping
    public String signup() {
        return "signup";
    }

    /**
     * 회원가입 페이지에 대한 GET 요청 처리
     *
     * @param signup 새로운 사용자 등록을 위한 빈 {@link Signup} 객체
     * @return 회원가입 페이지
     */
    @GetMapping("/user")
    public String signupUser(@ModelAttribute Signup signup) {
        return "user/signup";
    }

    /**
     * 회원가입 페이지에 대한 Post 요청을 처리하고, 유효성 검사 수행
     * <p> 사용자가 입력한 회원가입 데이터를 받아 유효성 검사를 진행합니다.
     * <p> 문제가 있는 경우 회원가입 페이지로 다시 반환하고,
     * 문제가 없는 경우 {@link Signup} 객체를 저장한 후
     * 메인 페이지로 리다이렉트합니다.
     *
     * @param signup 입력받은 회원 정보
     * @param bindingResult 유효성 검사 결과를 담고 있는 {@link BindingResult} 객체
     * @return 유효성 검사가 실패하면 회원가입 페이지, 성공하면 메인 페이지로 리다이렉트
     */
    @PostMapping("/user")
    public String saveUserAccount(@Valid @ModelAttribute Signup signup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        signupService.saveUserAccount(signup);

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
    @PostMapping("/check-username")
    public ResponseEntity<Map<String, String>> checkUsername(@Valid @RequestBody Username username, BindingResult bindingResult) {
        Map<String, String> response = new LinkedHashMap<>();

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError("username")).getDefaultMessage();
            response.put("error", errorMessage);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/seller")
    public String signupSeller(@ModelAttribute("signupSeller") SignupSeller signupSeller, Model model) {
        addBankNames(model);
        addLocalNumberValue(model);
        return "seller/signup";
    }

    @PostMapping("/seller")
    public String saveSellerAccount(@Valid @ModelAttribute SignupSeller signupSeller, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addBankNames(model);
            addLocalNumberValue(model);
            return "seller/signup";
        }
        return "redirect:/";
    }

    private void addBankNames(Model model) {
        List<String> bankNames = signupService.getBankNames();
        model.addAttribute("bankNames", bankNames);
    }

    private void addLocalNumberValue(Model model) {
        List<EnumMapperValue> allNumberList = Stream.concat(localNumberList.stream(), phonePrefixList.stream()).toList();
        model.addAttribute("allNumberType", allNumberList);
    }
}

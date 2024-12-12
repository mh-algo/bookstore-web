package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.publicpage.controller.dto.signup.Signup;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.SignupSeller;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.Username;
import com.bookshelf.bookproject.enums.EnumMapper;
import com.bookshelf.bookproject.enums.EnumMapperValue;
import com.bookshelf.bookproject.publicpage.service.SignupService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

import static com.bookshelf.bookproject.enums.EnumKeys.*;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final EnumMapper enumMapper;
    private final SignupService signupService;
    private List<EnumMapperValue> emailAddressList;
    private List<EnumMapperValue> phonePrefixList;
    private List<EnumMapperValue> localNumberList;

    @PostConstruct
    private void initialize() {
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
        return "public-page/signup";
    }

    /**
     * 회원가입 페이지에 대한 GET 요청 처리
     *
     * @param signup 새로운 사용자 등록을 위한 빈 {@link Signup} 객체
     * @return 회원가입 페이지
     */
    @GetMapping("/user")
    public String signupUser(@ModelAttribute Signup signup) {
        return "public-page/signup-user";
    }

    /**
     * 회원가입 페이지에 대한 Post 요청을 처리하고, 유효성 검사 수행
     * <p> 사용자가 입력한 회원가입 데이터를 받아 유효성 검사를 진행합니다.
     * <p> 문제가 있는 경우 회원가입 페이지를 반환하고,
     * 문제가 없는 경우 {@link Signup} 객체를 저장한 후
     * 메인 페이지로 리다이렉트합니다.
     *
     * @param signup 입력받은 회원가입 정보
     * @param bindingResult 유효성 검사 결과를 담고 있는 {@link BindingResult} 객체
     * @return 유효성 검사가 실패하면 회원가입 페이지, 성공하면 메인 페이지로 리다이렉트
     */
    @PostMapping("/user")
    public String saveUserAccount(@Valid @ModelAttribute Signup signup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "public-page/signup-user";
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

    /**
     * 판매자 가입 페이지에 대한 GET 요청 처리
     * <p> 빈 {@link SignupSeller} 객체와 은행 목록, 지역 번호 값을 뷰에 전달합니다.
     *
     * @param signupSeller 새로운 판매자 등록을 위한 빈 {@link SignupSeller} 객체
     * @param model 뷰에 전달할 객체를 담는 {@link Model} 객체
     * @return 판매자 가입 페이지
     */
    @GetMapping("/seller")
    public String signupSeller(@ModelAttribute SignupSeller signupSeller, Model model) {
        addBankNames(model);
        addAllNumberValue(model);
        return "public-page/signup-seller";
    }

    /**
     * 판매자 가입 페이지에 대한 Post 요청을 처리하고, 유효성 검사 수행
     * <p> 사용자가 입력한 가입 데이터를 유효성 검사하고, 검사에 실패하면 가입 페이지를 다시 반환합니다.
     * 유효성 검사가 성공하면 판매자 정보를 저장하고 메인 페이지로 리다이렉트합니다.
     *
     * @param signupSeller 입력받은 판매자 가입 정보
     * @param bindingResult 유효성 검사 결과를 담고 있는 {@link BindingResult} 객체
     * @param model 뷰에 전달할 객체를 담는 {@link Model} 객체
     * @return 유효성 검사가 실패하면 판매자 가입 페이지, 성공하면 메인 페이지로 리다이렉트
     */
    @PostMapping("/seller")
    public String saveSellerAccount(@Valid @ModelAttribute SignupSeller signupSeller, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addBankNames(model);
            addAllNumberValue(model);
            return "public-page/signup-seller";
        }
        signupService.saveSellerAccount(signupSeller);

        return "redirect:/";
    }

    /**
     * 은행 이름 리스트를 가져와 {@link Model} 객체에 담는 메서드
     * <p> 이 메서드는 뷰에서 은행 이름을 선택할 수 있도록
     * 모델에 "bankNames"라는 키로 은행 이름 리스트를 추가합니다.
     *
     * @param model 뷰에 전달할 객체를 담는 {@link Model} 객체
     */
    private void addBankNames(Model model) {
        List<String> bankNames = signupService.getBankNames();
        model.addAttribute("bankNames", bankNames);
    }

    /**
     * 지역 번호와 휴대전화 앞자리 번호 리스트를 가져와 {@link Model} 객체에 담는 메서드
     * <p> 이 메서드는 뷰에서 선택할 수 있도록 지역 번호와 휴대전화 앞자리 번호 리스트를
     * 모델에 "allNumberType"이라는 키로 추가합니다.
     *
     * @param model 뷰에 전달할 객체를 담는 {@link Model} 객체
     */
    private void addAllNumberValue(Model model) {
        List<EnumMapperValue> allNumberList = Stream.concat(localNumberList.stream(), phonePrefixList.stream()).toList();
        model.addAttribute("allNumberType", allNumberList);
    }
}

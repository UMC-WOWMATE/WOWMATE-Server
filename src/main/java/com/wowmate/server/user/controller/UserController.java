package com.wowmate.server.user.controller;

import com.wowmate.server.user.dto.*;
import com.wowmate.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {

        log.info("[signIn] 로그인을 시도하고 있습니다. Email : {}, pw : ****", signInRequestDto.getEmail());
        SignInResultDto signInResultDto = userService.signIn(signInRequestDto);

        if (signInResultDto.getCode() == 0) {
            log.info("[signIn] 정상적으로 로그인되었습니다. Email : {}, token : {}", signInRequestDto.getEmail(), signInResultDto.getToken());
        }

        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){

        log.info("[signUp] 회원가입을 수행합니다. Email : {}, password : ****, 학교 : {}, 전화번호 : {}, 생년월일 : {}, 성별 : {}, 권한 : {}"
                ,signUpRequestDto.getEmail(), signUpRequestDto.getUniv(), signUpRequestDto.getPhoneNumber()
                ,signUpRequestDto.getBirth(), signUpRequestDto.getGender(), signUpRequestDto.getRole());

        SignUpResultDto signUpResultDto = userService.signUp(signUpRequestDto);

        log.info("[signUp] 회원가입을 완료했습니다. Email : {}", signUpRequestDto.getEmail());
        return signUpResultDto;
    }

    @GetMapping(value = "/exception")
    public void exceptionResponse() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(RuntimeException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
		map.put("message", "에러 발생");
        return new ResponseEntity<>(map, headers, httpStatus);
    }
}

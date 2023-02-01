package com.wowmate.server.user.service.impl;

import com.wowmate.server.config.common.CommonResponse;
import com.wowmate.server.config.security.JwtTokenProvider;
import com.wowmate.server.user.domain.Gender;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.dto.SignInRequestDto;
import com.wowmate.server.user.dto.SignInResultDto;
import com.wowmate.server.user.dto.SignUpRequestDto;
import com.wowmate.server.user.dto.SignUpResultDto;
import com.wowmate.server.user.repository.UserRepository;
import com.wowmate.server.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public final UserRepository userRepository;
    public final JwtTokenProvider jwtTokenProvider;
    public final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public SignUpResultDto signUp(SignUpRequestDto signUpRequestDto)
//            String email, String password, String univ, Long phoneNumber, LocalDate birth, Gender gender, String role)
    {
        log.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if (signUpRequestDto.getRole().equalsIgnoreCase("admin")) {
            user = User.builder()
                    .email(signUpRequestDto.getEmail())
                    .univ(signUpRequestDto.getUniv())
                    .phoneNumber(phone_format(signUpRequestDto.getPhoneNumber()))
                    .birth(signUpRequestDto.getBirth())
                    .gender(signUpRequestDto.getGender())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
            /**
             * Spring Security에서 Role의 경우에는 prefix로 "ROLE_" 이라는 문자를 붙인다.
             * 따라서 다음의 둘 중 하나를 선택해서 적용해야 한다.
             *
             * 1. DB상 role 정보에 prefix로 "ROLE_"을 붙여준다.
             * 2. hasRole 대신 hasAuthority를 사용한다.
             *
             * hasRole 안에 "ROLE_" prefix를 직접 넣어주면 spring security가 넣어줘서 판단하니 직접 넣지 말라는 컴파일 에러도 발생한다.
             */
        } else {
            user = User.builder()
                    .email(signUpRequestDto.getEmail())
                    .univ(signUpRequestDto.getUniv())
                    .phoneNumber(phone_format(signUpRequestDto.getPhoneNumber()))
                    .birth(signUpRequestDto.getBirth())
                    .gender(signUpRequestDto.getGender())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto();
        log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");

        if (!savedUser.getEmail().isEmpty()) {
            log.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            log.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {

        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.findByEmail(signInRequestDto.getEmail()).get();

        log.info("[getSignInResult] Email : {}", signInRequestDto.getEmail());
        log.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("패스워드 불일치");
        }
        log.info("[getSignInResult] 패스워드 일치");

        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getEmail()), user.getRoles()))
                .build();
        log.info("[getSignInResult] SignInResultDto 객체에 값 주입 완료");

        setSuccessResult(signInResultDto);
        return signInResultDto;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    private String phone_format(String number) {
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";
        return number.replaceAll(regEx, "$1-$2-$3");
    }
}

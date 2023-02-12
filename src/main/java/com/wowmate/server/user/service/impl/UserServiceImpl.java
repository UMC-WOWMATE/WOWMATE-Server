package com.wowmate.server.user.service.impl;

import com.wowmate.server.config.common.CommonResponse;
import com.wowmate.server.config.security.JwtTokenProvider;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.dto.*;
import com.wowmate.server.user.repository.UserRepository;
import com.wowmate.server.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.wowmate.server.response.ResponseStatus.*;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public SignUpResultDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("[getSignUpResult] 회원 가입 정보 전달");

        SignUpResultDto signUpResultDto = new SignUpResultDto();
        System.out.println(userRepository.findByEmail(signUpRequestDto.getEmail()));
        if (!userRepository.findByEmail(signUpRequestDto.getEmail()).isEmpty()){
            log.info("[checkDuplicateSignUp] 중복 회원 가입");
            setDuplicateResult(signUpResultDto);
            return signUpResultDto;
        }

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
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException, BaseException {

        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.findByEmail(signInRequestDto.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

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

    public UserInfoDto getUserInfo(User currentUser) {

        UserInfoDto userInfo = UserInfoDto.builder()
                .email(currentUser.getEmail())
                .univ(currentUser.getUniv())
                .phoneNumber(currentUser.getPhoneNumber())
                .birth(currentUser.getBirth())
                .gender(currentUser.getGender())
                .build();

        return userInfo;
    }

    public void updatePassword(UpdatePasswordDto updatePasswordDto) throws BaseException{

        Optional<User> user = userRepository.findByEmail(updatePasswordDto.getEmail());

        if(user.isEmpty()) {
            throw new BaseException(NOT_EXIST_USER);
        }
        log.info("[updatePassword] 비밀번호 변경");
        user.get().updatePassword(passwordEncoder.encode(updatePasswordDto.getNew_password()));
        userRepository.save(user.get());
        log.info("[updatePassword] 비밀번호 변경 완료");
    }

    public void deleteUser(User currentUser) throws BaseException{

        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

        userRepository.deleteById(user.getId());
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(SignUpResultDto result) {
        result.setIsSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(SignUpResultDto result) {
        result.setIsSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMessage(CommonResponse.FAIL.getMsg());
    }

    private void setDuplicateResult(SignUpResultDto result) {
        result.setIsSuccess(false);
        result.setCode(CommonResponse.DUPLICATION.getCode());
        result.setMessage(CommonResponse.DUPLICATION.getMsg());
    }

    private String phone_format(String number) {
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";
        return number.replaceAll(regEx, "$1-$2-$3");
    }
}

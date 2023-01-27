package com.wowmate.server.user.service;

import com.wowmate.server.user.domain.Gender;
import com.wowmate.server.user.dto.SignInRequestDto;
import com.wowmate.server.user.dto.SignInResultDto;
import com.wowmate.server.user.dto.SignUpRequestDto;
import com.wowmate.server.user.dto.SignUpResultDto;

import java.time.LocalDate;

public interface UserService {

    SignUpResultDto signUp(SignUpRequestDto signUpRequestDto);
            //String email, String password, String univ, Long phoneNumber, LocalDate birth, Gender gender, String role);
    SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;

}

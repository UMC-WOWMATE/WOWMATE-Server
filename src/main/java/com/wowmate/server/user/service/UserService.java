package com.wowmate.server.user.service;

import com.wowmate.server.response.BaseException;
import com.wowmate.server.user.domain.Gender;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.dto.*;

import java.time.LocalDate;

public interface UserService {

    SignUpResultDto signUp(SignUpRequestDto signUpRequestDto);
    SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException, BaseException;
    UserInfoDto getUserInfo(User currentUser);
    void updatePassword(UpdatePasswordDto updatePasswordDto) throws  BaseException;
}

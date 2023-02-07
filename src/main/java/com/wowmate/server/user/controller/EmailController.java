package com.wowmate.server.user.controller;

import com.wowmate.server.post.dto.PostRegisterResDto;
import com.wowmate.server.response.Response;
import com.wowmate.server.user.dto.AuthCodeDto;
import com.wowmate.server.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final EmailService emailService;

    @PostMapping("/emailConfirm")
    public Response<AuthCodeDto, Object> emailConfirm(@RequestParam String email) throws Exception {

        log.info("[emailConfirm] 이메일 인증을 시도하고 있습니다. Email : {}", email);

        AuthCodeDto authCodeDto = new AuthCodeDto();
        authCodeDto.setCode(emailService.sendSimpleMessage(email));

        return new Response<>(authCodeDto);
    }
}

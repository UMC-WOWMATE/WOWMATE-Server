package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.domain.MessageType;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.service.ChatroomService;
import com.wowmate.server.chatroom.service.MessageService;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.Response;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Message", description = "메세지 API")
public class MessageController {

    private final MessageService messageService;

    @Operation(tags = "Message", description = "메세지 전송")
    @MessageMapping(value = "/chat/message")
    public Response<Object, Object> sendMessage(@RequestBody MessageDto messageDto) {

        try {

            messageService.sendMessage(messageDto);
            return new Response<>(ResponseStatus.SUCCESS); // 이렇게 말고 다른 방법?

        } catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }

    }

//    @Operation(tags = "Message", description = "매칭 수락")
//    @MessageMapping(value = "/chat/match/")
//    public Response<Object, Object> matchRespond(@AuthenticationPrincipal User user, @RequestBody String roomUuid) {
//
//        try {
//            messageService.matchRespond(user, roomUuid);
//            return new Response<>(ResponseStatus.SUCCESS);
//        } catch (BaseException e) {
//            return new Response<>(e.getResponseStatus());
//        }
//
//    }

}

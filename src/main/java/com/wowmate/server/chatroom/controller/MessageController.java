package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.domain.MessageType;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.service.ChatroomService;
import com.wowmate.server.chatroom.service.MessageService;
import com.wowmate.server.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Message", description = "메세지 API")
public class MessageController {

    private final SimpMessagingTemplate template;
    private final ChatroomService chatroomService;

    private final MessageService messageService;

    @Operation(tags = "Message", description = "메세지 전송")
    @MessageMapping(value = "/chat/message")
    public void sendMessage(User user, @RequestBody MessageDto messageDto) {

        if (messageDto.getMessageType().equals(MessageType.ENTER)) {
            chatroomService.createChatroom(messageDto.getPostId(), user);
        }

        messageService.createMessage(user, messageDto);

        template.convertAndSend("/sub/chats/" + messageDto.getChatroomUuid(), messageDto);

    }

//    @Operation(tags = "Message", description = "매칭 요청")
//    @MessageMapping(value = "/chat/match/request")
//    public void matchRequest(User user, @RequestBody String roomUuid) {
//
//
//    }

    @Operation(tags = "Message", description = "매칭 성사")
    @MessageMapping(value = "/chat/match/")
    public void matchRespond(User user, @RequestBody String roomUuid) {

        List<MatchMessageDto> matchMessageDtos = messageService.matchRespond(user, roomUuid);

        template.convertAndSend("/sub/chats/" + roomUuid, matchMessageDtos.get(0));
        template.convertAndSend("/sub/chats/" + roomUuid, matchMessageDtos.get(1));

    }


}

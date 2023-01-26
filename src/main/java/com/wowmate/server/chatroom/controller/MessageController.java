package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.domain.MessageType;
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

        if(messageDto.getMessageType().equals(MessageType.ENTER)) {
            chatroomService.createChatroom(messageDto.getPostId(), user);
        }

        messageService.createMessage(user, messageDto);

        template.convertAndSend("/sub/chats/" + messageDto.getChatroomUuid(), messageDto);
    }

}

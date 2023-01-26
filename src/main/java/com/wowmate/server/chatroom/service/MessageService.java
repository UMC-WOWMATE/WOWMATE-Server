package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.MessageRepository;
import com.wowmate.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;

    public void createMessage(User user, MessageDto messageDto) {
        Chatroom chatroom = chatroomRepository.findByUuid(messageDto.getChatroomUuid()).orElseThrow(EntityNotFoundException::new);

        Message message = Message.builder()
                .messageType(messageDto.getMessageType())
                .sendUserId(user.getId())
                .content(messageDto.getContent())
                .chatroom(chatroom)
                .build();

        messageRepository.save(message);
        chatroom.getMessages().add(message);
    }



}

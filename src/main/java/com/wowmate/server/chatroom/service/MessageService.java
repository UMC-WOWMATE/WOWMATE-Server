package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.domain.MessageType;
import com.wowmate.server.chatroom.domain.UserChatroom;
import com.wowmate.server.chatroom.dto.GetMessageDto;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.MessageRepository;
import com.wowmate.server.chatroom.repository.UserChatroomRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageService {

    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    
    private final UserChatroomRepository userChatroomRepository;
    private final UserRepository userRepository;

    public void sendMessage(MessageDto messageDto) throws BaseException {

        User user = userRepository.findByEmail(messageDto.getSenderEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

        Chatroom chatroom = chatroomRepository.findByUuid(messageDto.getChatroomUuid())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        if(messageDto.getMessageType().equals(MessageType.ENTER)) {
            log.info("{}: 첫 메시지 전송" , messageDto.getSenderEmail());
        } else {
            log.info("{}: 메시지 전송", messageDto.getSenderEmail());
        }

        Message message = Message.builder()
                .messageType(messageDto.getMessageType())
                .senderEmail(user.getEmail())
                .content(messageDto.getContent())
                .chatroom(chatroom)
                .matchInfo(null)
                .build();

        messageRepository.save(message);
        chatroom.getMessages().add(message);

        GetMessageDto getMessageDto = GetMessageDto.builder()
                .content(message.getContent())
                .sendTime(message.getCreatedDate())
                .senderEmail(message.getSenderEmail())
                .messageType(message.getMessageType())
                .build();

        template.convertAndSend("/sub/chats/" + messageDto.getChatroomUuid(), getMessageDto);

    }
    
}

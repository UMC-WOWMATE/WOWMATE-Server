package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.domain.MessageType;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.MessageRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    public void sendMessage(MessageDto messageDto) throws BaseException {

        User user = userRepository.findByEmail(messageDto.getSenderEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

        Chatroom chatroom = chatroomRepository.findByUuid(messageDto.getChatroomUuid())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        Message message = Message.builder()
                .messageType(messageDto.getMessageType())
                .senderEmail(user.getEmail())
                .content(messageDto.getContent())
                .chatroom(chatroom)
                .build();

        messageRepository.save(message);
        chatroom.getMessages().add(message);

        template.convertAndSend("/sub/chats/" + messageDto.getChatroomUuid(), messageDto);

    }

    public void matchRespond(User user, String roomUuid) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        MatchMessageDto matchMessageDto = MatchMessageDto.builder()
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .build();

        Message message = Message.builder()
                .messageType(MessageType.MATCH)
                .senderEmail(user.getEmail())
                .content(matchMessageDto.toString())
                .chatroom(chatroom)
                .build();

        messageRepository.save(message);

        template.convertAndSend("/sub/chats/" + roomUuid, matchMessageDto);

    }



}

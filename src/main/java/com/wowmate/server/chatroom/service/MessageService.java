package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.domain.UserChatroom;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

        List<MatchMessageDto> matchMessageDtos = new ArrayList<>();

        MatchMessageDto matchMessageDto1 = MatchMessageDto.builder()
                .School(user.getUniv())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .build();

        matchMessageDtos.add(matchMessageDto1);

        UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        User opponentUser = userRepository.findByEmail(chatroom.getOpponentUserEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

        MatchMessageDto matchMessageDto2 = MatchMessageDto.builder()
                .School(opponentUser.getUniv())
                .age(opponentUser.getAge())
                .phoneNumber(opponentUser.getPhoneNumber())
                .gender(opponentUser.getGender().toString())
                .build();

        matchMessageDtos.add(matchMessageDto2);

        template.convertAndSend("/sub/chats/" + roomUuid, matchMessageDtos.get(0));
        template.convertAndSend("/sub/chats/" + roomUuid, matchMessageDtos.get(1));

    }



}

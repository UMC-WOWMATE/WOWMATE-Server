package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.dto.MessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.MessageRepository;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;

    private final UserRepository userRepository;

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

    public List<MatchMessageDto> matchRespond(User user, String roomUuid) {

        List<MatchMessageDto> matchMessageDtos = new ArrayList<>();

        MatchMessageDto matchMessageDto1 = MatchMessageDto.builder()
                .School(user.getUniv())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .build();
        matchMessageDtos.add(matchMessageDto1);

        String opponentUserEmail = chatroomRepository.findByChatroomUuidAndUserEmail(roomUuid, user.getEmail()).getOpponentUserEmail();
        User opponentUser = userRepository.findByEmail(opponentUserEmail);

        MatchMessageDto matchMessageDto2 = MatchMessageDto.builder()
                .School(opponentUser.getUniv())
                .age(opponentUser.getAge())
                .phoneNumber(opponentUser.getPhoneNumber())
                .gender(opponentUser.getGender().toString())
                .build();
        matchMessageDtos.add(matchMessageDto2);

        return matchMessageDtos;

    }

}

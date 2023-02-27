package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.*;
import com.wowmate.server.chatroom.dto.GetMessageDto;
import com.wowmate.server.chatroom.dto.MatchMessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.MessageRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MatchService {

    private final SimpMessagingTemplate template;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;


    public void matchRequest(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        // if 매칭 전
        // if else 매칭 대기
        // else 매칭 완료
        if (chatroom.getMatchType().equals(MatchType.YET)) {

            GetMessageDto getMessageDto = ifMatchYet(user, chatroom);

            log.info("{}: 매칭 요청 전송", user.getEmail());

            template.convertAndSend("/sub/chats/" + roomUuid, getMessageDto);

        } else if (chatroom.getMatchType().equals(MatchType.READY)) {

            MatchMessageDto postUserInfoDto = ifMatchReady(chatroom.getPost().getUser(), chatroom);
            MatchMessageDto requestUserInfoDto = ifMatchReady(chatroom.getRequestUser(), chatroom);

            log.info("{}: 매칭 요청 수락", user.getEmail());

            template.convertAndSend("/sub/chats/{}" + roomUuid, postUserInfoDto);
            template.convertAndSend("/sub/chats/{}" + roomUuid, requestUserInfoDto);

        } else {
           throw new BaseException(ResponseStatus.ALREADY_MATCH);
        }

    }

    private GetMessageDto ifMatchYet(User user, Chatroom chatroom) {

        chatroom.setMatchType(MatchType.READY);

        Message message = Message.builder()
                .chatroom(chatroom)
                .messageType(MessageType.MATCH_REQ)
                .senderEmail(user.getEmail())
                .content("상대방이 매칭 요청을 보냈습니다. 수락하시려면 하단 매칭 버튼을 눌러주세요.")
                .matchInfo(null)
                .build();

        messageRepository.save(message);
        chatroom.getMessages().add(message);

        GetMessageDto getMessageDto = GetMessageDto.builder()
                .senderEmail(user.getEmail())
                .sendTime(message.getCreatedDate())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .build();

        return getMessageDto;

    }

    private MatchMessageDto ifMatchReady(User user, Chatroom chatroom) {

        chatroom.setMatchType(MatchType.SUCCESS);

        Message infoMessage = Message.builder()
                .messageType(MessageType.MATH_INFO)
                .senderEmail(user.getEmail())
                .content("매칭 성공!")
                .chatroom(chatroom)
                .matchInfo(MatchInfo.builder()
                        .age(user.getAge())
                        .gender(user.getGender())
                        .phoneNumber(user.getPhoneNumber())
                        .build()
                )
                .build();

        messageRepository.save(infoMessage);
        chatroom.getMessages().add(infoMessage);

        MatchMessageDto infoMessageDto = MatchMessageDto.builder()
                .senderEmail(infoMessage.getSenderEmail())
                .age(infoMessage.getMatchInfo().getAge())
                .phoneNumber(infoMessage.getMatchInfo().getPhoneNumber())
                .gender(infoMessage.getMatchInfo().getGender())
                .sendTime(infoMessage.getCreatedDate())
                .content(infoMessage.getContent())
                .messageType(infoMessage.getMessageType())
                .build();

        return infoMessageDto;

    }


}

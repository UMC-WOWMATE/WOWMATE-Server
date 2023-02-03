package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.Message;
import com.wowmate.server.chatroom.domain.MessageType;
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

        UserChatroom userChatroom = checkExitUserChatroom(messageDto.getChatroomUuid(), messageDto.getReceiverEmail());

        // 상대 유저가 채팅방을 삭제했으면 다시 만들어줌
        if(userChatroom == null) {
            User receiveUser = userRepository.findByEmail(messageDto.getReceiverEmail())
                    .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

            UserChatroom requestUserChatroom = UserChatroom.builder()
                    .chatroom(chatroom)
                    .user(receiveUser)
                    .opponentUserEmail(messageDto.getSenderEmail())
                    .opponentUserImg(chatroom.getPost().getUser().getImage())
                    .build();

            userChatroomRepository.save(requestUserChatroom);
            chatroom.getUserChatrooms().add(requestUserChatroom);
        }

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

    private UserChatroom checkExitUserChatroom(String roomUuid, String email) {

        List<UserChatroom> chatrooms = userChatroomRepository.findByUserEmail(email);

        UserChatroom findChatroom = null;

        for (UserChatroom chatroom : chatrooms) {
            if(chatroom.getUser().getEmail().equals(email)) {
                findChatroom = chatroom;
                break;
            }
        }

        return findChatroom;

    }

    private void createUserChatroom(Chatroom chatroom, MessageDto messageDto) {





    }

}

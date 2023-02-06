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

//         상대 유저가 채팅방을 삭제했으면 다시 만들어줌 -> 삭제 없음...
//        UserChatroom userChatroom = checkExitUserChatroom(messageDto.getChatroomUuid(), messageDto.getReceiverEmail());


//        if(userChatroom == null) {
//            createUserChatroom(messageDto, chatroom);
//        }

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
                .build();

        template.convertAndSend("/sub/chats/" + messageDto.getChatroomUuid(), getMessageDto);

    }

//    private UserChatroom checkExitUserChatroom(String roomUuid, String email) {
//
//        List<UserChatroom> chatrooms = userChatroomRepository.findByUserEmail(email);
//
//        UserChatroom findChatroom = null;
//
//        for (UserChatroom chatroom : chatrooms) {
//            if(chatroom.getUser().getEmail().equals(email)) {
//                findChatroom = chatroom;
//                break;
//            }
//        }
//
//        return findChatroom;
//
//    }

//    private void createUserChatroom(MessageDto messageDto, Chatroom chatroom) throws BaseException {
//        User receiveUser = userRepository.findByEmail(messageDto.getReceiverEmail())
//                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));
//
//        UserChatroom requestUserChatroom = UserChatroom.builder()
//                .chatroom(chatroom)
//                .user(receiveUser)
//                .opponentUserEmail(messageDto.getSenderEmail())
//                .opponentUserImg(chatroom.getPost().getUser().getImage())
//                .build();
//
//        userChatroomRepository.save(requestUserChatroom);
//        chatroom.getUserChatrooms().add(requestUserChatroom);
//    }
    
}

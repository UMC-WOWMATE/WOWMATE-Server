package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.UserChatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.dto.GetChatroomValidDto;
import com.wowmate.server.chatroom.dto.GetMessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.UserChatroomRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    private final UserChatroomRepository userChatroomRepository;

    private final PostRepository postRepository;


    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<GetChatroomListDto> getChatroomList(User user) throws BaseException {

        userValidate(user);

        List<GetChatroomListDto> chatroomListDtos = userChatroomRepository.findByUserEmail(user.getEmail())
                .stream()
                .map(
                        userChatroom ->
                                GetChatroomListDto.builder()
                                        .roomUuid(userChatroom.getChatroom().getUuid())
                                        .postTitle(userChatroom.getChatroom().getPost().getTitle())
                                        .opponentUserImg(userChatroom.getOpponentUserImg())
                                        .lastMessage(
                                                (userChatroom.getChatroom().getMessages().isEmpty()) ?
                                                        null : (userChatroom.getChatroom().getMessages().get(userChatroom.getChatroom().getMessages().size() - 1).getContent())
                                        )
                                        .lastMessageDate(
                                                (userChatroom.getChatroom().getMessages().isEmpty()) ?
                                                        null : (userChatroom.getChatroom().getMessages().get(userChatroom.getChatroom().getMessages().size() - 1).getCreatedDate())
                                        )
                                        .build() // 원래 로직에서는 메시지를 보내야 채팅방이 만들어지므로 null이 될리가 없음 만약 null이면 예외처리를 해야함
                )
                //.sorted(Comparator.comparing(GetChatroomListDto::getLastMessageDate).reversed()) // 메시지 날짜가 null이어서 주석표시
                .collect(Collectors.toList());

        return chatroomListDtos;

    }

    // 특정 채팅방 조회
    @Transactional(readOnly = true)
    public GetChatroomDto getChatroom(String roomUuid, User user) throws BaseException {

        userValidate(user);

        UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));


        GetChatroomDto chatroomDto = GetChatroomDto.builder()
                .postTitle(chatroom.getChatroom().getPost().getTitle())
                .createdDate(chatroom.getCreatedDate())
                .messageList(
                        chatroom.getChatroom().getMessages().stream()
                                .map(
                                        message -> GetMessageDto.builder()
                                                .senderEmail(message.getSenderEmail())
                                                .content(message.getContent())
                                                .sendTime(message.getCreatedDate())
                                                .messageType(message.getMessageType())
                                                .build()
                                ).collect(Collectors.toList())
                )
                .opponentEmail(chatroom.getOpponentUserEmail())
                .userEmail(chatroom.getUser().getEmail())
                .opponentImg(chatroom.getOpponentUserImg())
                .postCategory(chatroom.getChatroom().getPost().getCategoryName())
                .build();

        return chatroomDto;

    }


    // 채팅방 삭제
    public List<GetChatroomListDto> deleteChatroom(String roomUuid, User user) throws BaseException {

        userValidate(user);

        UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        userChatroomRepository.delete(chatroom);

        List<GetChatroomListDto> chatroomListDtos = getChatroomList(user);

        return chatroomListDtos;

    }

    public GetChatroomValidDto chatroomValid(Long postId, User user) throws BaseException {

        userValidate(user);
        postValidate(postId);

        Chatroom chatroom = checkExistChatroom(postId, user);

        GetChatroomValidDto getChatroomValidDto;

        if(chatroom == null) {
            log.info("만들어진 채팅방 존재하지 않음. roomUuid 빈 스트링 반환");
            getChatroomValidDto = GetChatroomValidDto.builder()
                    .roomUuid("")
                    .isBlocked(false)
                    .build();
        } else {

            if(chatroom.getIsBlocked().equals(true)) {
                log.info("이미 채팅방 차단된 채팅방, 채팅방 생성 불가");
                getChatroomValidDto = GetChatroomValidDto.builder()
                        .roomUuid("")
                        .isBlocked(true)
                        .build();
            } else {
                log.info("채팅 중복 생성 불가, 기존 채팅방 반환");
                getChatroomValidDto = GetChatroomValidDto.builder()
                        .roomUuid(chatroom.getUuid())
                        .isBlocked(false)
                        .build();
            }
            
        }

        return getChatroomValidDto;

    }


    // 채팅방 생성
    public GetChatroomDto createChatroom(Long postId, User user) throws BaseException {

        userValidate(user);

        Post post = postValidate(postId);

        Chatroom chatroom = new Chatroom(post, user);
        chatroomRepository.save(chatroom);

        UserChatroom requestUserChatroom = createUserChatroom(chatroom);

        GetChatroomDto chatroomDto = GetChatroomDto.builder()
                .userEmail(user.getEmail())
                .postTitle(chatroom.getPost().getTitle())
                .createdDate(requestUserChatroom.getCreatedDate())
                .messageList(
                        chatroom.getMessages().stream()
                                .map(
                                        message -> GetMessageDto.builder()
                                                .senderEmail(message.getSenderEmail())
                                                .content(message.getContent())
                                                .sendTime(message.getCreatedDate())
                                                .messageType(message.getMessageType())
                                                .build()
                                ).collect(Collectors.toList())
                )
                .opponentEmail(requestUserChatroom.getOpponentUserEmail())
                .opponentImg(requestUserChatroom.getOpponentUserImg())
                .postCategory(chatroom.getPost().getCategoryName())
                .build();

        return chatroomDto;

    }

    public List<GetChatroomListDto> blockChatroom(String roomUuid, User user) throws BaseException {

        userValidate(user);

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        // 채팅 차단시 유저 채팅방과 상대방 유저 채팅방 모두 삭제
        userChatroomRepository.delete(chatroom.getUserChatrooms().get(0));
        userChatroomRepository.delete(chatroom.getUserChatrooms().get(1));

        chatroom.blockChatroom();

        return getChatroomList(user);

    }

    private void userValidate(User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

    }

    private Post postValidate(Long postId) throws BaseException {

        return postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));

    }

    private Chatroom checkExistChatroom(Long postId, User user) throws BaseException {

        return chatroomRepository.findByPostIdAAndRequestUserEmail(postId, user.getEmail())
                .orElse(null);

    }

    // 유저 채팅방 2개 생성 후 요청 사용자 채팅방 반환
    private UserChatroom createUserChatroom(Chatroom chatroom) {

        UserChatroom postUserChatroom = UserChatroom.builder()
                .chatroom(chatroom)
                .user(chatroom.getPost().getUser())
                .opponentUserEmail(chatroom.getRequestUser().getEmail())
                .opponentUserImg(chatroom.getRequestUser().getImageUrl())
                .build();

        userChatroomRepository.save(postUserChatroom);
        chatroom.getUserChatrooms().add(postUserChatroom);

        UserChatroom requestUserChatroom = UserChatroom.builder()
                .chatroom(chatroom)
                .user(chatroom.getRequestUser())
                .opponentUserEmail(chatroom.getPostUserEmail())
                .opponentUserImg(chatroom.getPost().getUser().getImageUrl())
                .build();

        userChatroomRepository.save(requestUserChatroom);
        chatroom.getUserChatrooms().add(requestUserChatroom);

        return requestUserChatroom;

    }



}

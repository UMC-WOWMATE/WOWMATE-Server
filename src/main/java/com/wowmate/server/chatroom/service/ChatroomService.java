package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.UserChatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.dto.GetMessageDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.UserChatroomRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
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

    private final UserRepository userRepository;

    private final PostRepository postRepository;


    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<GetChatroomListDto> getChatroomList(User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        List<GetChatroomListDto> chatroomListDtos = userChatroomRepository.findByUserEmail(user.getEmail())
                .stream()
                .map(
                        userChatroom ->
                                GetChatroomListDto.builder()
                                        .postTitle(userChatroom.getChatroom().getPost().getTitle())
                                        .opponentUserImg(userChatroom.getOpponentUserImg())
                                        .lastMessage(
                                                (userChatroom.getChatroom().getMessages().isEmpty()) ?
                                                        null : (userChatroom.getChatroom().getMessages().get(userChatroom.getChatroom().getMessages().size() - 1).getContent())
                                        )
                                        .lastMessageDate(
                                                (userChatroom.getChatroom().getMessages().isEmpty()) ?
                                                        null : (userChatroom.getChatroom().getMessages().get(userChatroom.getChatroom().getMessages().size() - 1).getCreatedDate()
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                                        )
                                        .build() // 원래 로직에서는 메시지를 보내야 채팅방이 만들어지므로 null이 될리가 없음 만약 null이면 예외처리를 해야함
                )
                .sorted(Comparator.comparing(GetChatroomListDto::getLastMessageDate))
                .collect(Collectors.toList());

        return chatroomListDtos;

    }


    // 특정 채팅방 조회
    @Transactional(readOnly = true)
    public GetChatroomDto getChatroom(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));


        GetChatroomDto chatroomDto = GetChatroomDto.builder()
                .postTitle(chatroom.getChatroom().getPost().getTitle())
                .createdDate(chatroom.getCreatedDate().format(DateTimeFormatter.ofPattern("채팅 생성일 yyyy.MM.dd")))
                .messageList(
                        chatroom.getChatroom().getMessages().stream()
                                .map(
                                        message -> GetMessageDto.builder()
                                                .senderEmail(message.getSenderEmail())
                                                .content(message.getContent())
                                                .sendTime(message.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                                                .build()
                                ).collect(Collectors.toList())
                )
                .opponentEmail(chatroom.getOpponentUserEmail())
                .opponentImg(chatroom.getOpponentUserImg())
                .postCategory(chatroom.getChatroom().getPost().getCategoryName()) // 추후 post에서 바로 category name 가져오는 걸로 변경
                .build();

        return chatroomDto;

    }


    // 채팅방 삭제
    public List<GetChatroomListDto> deleteChatroom(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        userChatroomRepository.delete(chatroom);

        List<GetChatroomListDto> chatroomListDtos = getChatroomList(user);

        return chatroomListDtos;

    }


    // 채팅방 생성
    public GetChatroomDto createChatroom(Long postId, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));

        // 생성된 채팅방이 없으면 createChatroom 로직 실행 x
        // 같은 유저가 채팅 중복 생성을 하면 기존 채팅방 반환
        Chatroom chatroom = checkExistChatroom(postId, user);

        if(chatroom != null) {
            GetChatroomDto getChatroomDto = getChatroom(chatroom.getUuid(), user);
            return getChatroomDto;
        }

        chatroom = new Chatroom(post, user);

        chatroomRepository.save(chatroom);

        UserChatroom requestUserChatroom = UserChatroom.builder()
                .chatroom(chatroom)
                .user(chatroom.getRequestUser())
                .opponentUserEmail(chatroom.getPostUserEmail())
                .opponentUserImg(chatroom.getPost().getUser().getImage())
                .build();

        UserChatroom postUserChatroom = UserChatroom.builder()
                .chatroom(chatroom)
                .user(chatroom.getPost().getUser())
                .opponentUserEmail(chatroom.getRequestUser().getEmail())
                .opponentUserImg(chatroom.getRequestUser().getImage())
                .build();

        userChatroomRepository.save(requestUserChatroom);
        userChatroomRepository.save(postUserChatroom);

        chatroom.getUserChatrooms().add(requestUserChatroom);
        chatroom.getUserChatrooms().add(postUserChatroom);


        GetChatroomDto getChatroomDto = this.getChatroom(chatroom.getUuid(), user);

        return getChatroomDto;

    }

    // 자기가 쓴 글에 채팅 안 만들어져야함 -> 이거는 클라에서 해줄 수 있나?


    private Chatroom checkExistChatroom(Long postId, User user) {

        List<Chatroom> chatrooms = chatroomRepository.findByPostId(postId);
        Chatroom findChatroom = null;

        for (Chatroom chatroom : chatrooms) {
            if(chatroom.getRequestUser().equals(user)) {
                findChatroom = chatroom;
                break;
            }
        }

        return findChatroom;
    }


}

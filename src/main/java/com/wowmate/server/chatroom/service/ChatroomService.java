package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.dto.MessageDto;
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

        List<GetChatroomListDto> chatroomListDtos = chatroomRepository.findChatroomsByRequestUserEmail(user.getEmail())
                .stream()
                .map(
                        chatroom ->
                                GetChatroomListDto.builder()
                                        .postTitle(chatroom.getPost().getTitle())
                                        .lastMessage(
                                                (chatroom.getMessages().isEmpty()) ?
                                                null : (chatroom.getMessages().get(chatroom.getMessages().size() - 1).getContent())
                                        )
                                        .lastMessageDate(
                                                (chatroom.getMessages().isEmpty()) ?
                                                null : (chatroom.getMessages().get(chatroom.getMessages().size() - 1).getCreatedDate()
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

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        GetChatroomDto chatroomDto = GetChatroomDto.builder()
                .postTitle(chatroom.getPost().getTitle())
                .createDate(chatroom.getCreatedDate().format(DateTimeFormatter.ofPattern("채팅 생성일 yyyy.MM.dd")))
                .messageList(chatroom.getMessages())
                //.opponentImg(chatroom.getOpponentUserEmail())
                .postCategory(chatroom.getPost().getCategory().getName()) // 추후 post에서 바로 category name 가져오는 걸로 변경
                .build();

        return chatroomDto;

    }


    // 채팅방 삭제
    public List<GetChatroomListDto> deleteChatroom(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        chatroomRepository.delete(chatroom);

        List<GetChatroomListDto> chatroomListDtos = getChatroomList(user);
        return chatroomListDtos;

    }


    // 채팅방 생성
    public GetChatroomDto createChatroom(MessageDto messageDto) throws BaseException {

        User user = userRepository.findByEmail(messageDto.getSenderEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));

        Post post = postRepository.findById(messageDto.getPostId())
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));

        Chatroom chatroom = new Chatroom(post, user);

        chatroomRepository.save(chatroom);

        GetChatroomDto getChatroomDto = getChatroom(chatroom.getUuid(), user);

        return getChatroomDto;

    }


    // post에 같은 유저가 채팅 중복 생성 금지
    // 자기가 쓴 글에 채팅 안 만들어져야함

}

package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.CreateChatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.CreateChatroomRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    private final CreateChatroomRepository createChatroomRepository;
    private final UserRepository userRepository;

    private final PostRepository postRepository;


    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<GetChatroomListDto> getChatroomList(User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        List<GetChatroomListDto> chatroomListDtos = chatroomRepository
                .findByUserEmail(user.getUsername())
                .stream()
                .map(
                        chatroom ->
                                GetChatroomListDto.builder()
                                        .postTitle(chatroom.getCreateChatroom().getPost().getTitle())
                                        .lastMessage(chatroom.getMessages().get(chatroom.getMessages().size() - 1).getContent())
                                        .lastMessageDate(chatroom.getMessages().get(chatroom.getMessages().size() - 1)
                                                .getCreatedDate().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                                        .build()
                )
                .sorted(Comparator.comparing(GetChatroomListDto::getLastMessageDate).reversed())
                .collect(Collectors.toList());

        return chatroomListDtos;

    }


    @Transactional(readOnly = true)
    // 특정 채팅방 조회
    public GetChatroomDto getChatroom(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        Chatroom chatroom = chatroomRepository
                .findByChatroomUuidAndUserEmail(roomUuid,user.getEmail())
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        GetChatroomDto chatroomDto = GetChatroomDto
                .builder()
                .postTitle(chatroom.getCreateChatroom().getPost().getTitle())
                .createDate(chatroom.getCreatedDate().format(DateTimeFormatter.ofPattern("채팅 생성일 yyyy.MM.dd")))
                .messageList(chatroom.getMessages())
                .opponentImg(userRepository.findByEmail(chatroom.getOpponentUserEmail()).getImage())
                .postCategory(chatroom.getCreateChatroom().getPost().getCategory().getName())
                .build();

        return chatroomDto;

    }


    // 채팅방 삭제
    public List<GetChatroomListDto> deleteChatroom(String roomUuid, User user) throws BaseException {

        if (user == null) {
            throw new BaseException(ResponseStatus.NOT_FOUND_USER);
        }

        // createChatroom에서 삭제해야하나?...흠

        Chatroom chatroom = chatroomRepository.findByUuid(roomUuid)
                .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

        chatroomRepository.delete(chatroom);

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

        CreateChatroom createChatroom = new CreateChatroom(post, user);
        createChatroomRepository.save(createChatroom);

        Chatroom chatroomForUser = Chatroom.createChatroomForUser(createChatroom);
        Chatroom chatroomForPostUser = Chatroom.createChatroomForPostUser(createChatroom);

        chatroomRepository.save(chatroomForUser);
        chatroomRepository.save(chatroomForPostUser);

        GetChatroomDto getChatroomDto = this.getChatroom(chatroomForUser.getUuid(), user);

        return getChatroomDto;

    }

    // post에 같은 유저가 채팅 중복 생성 금지
    // 자기가 쓴 글에 채팅 안 만들어져야함

}

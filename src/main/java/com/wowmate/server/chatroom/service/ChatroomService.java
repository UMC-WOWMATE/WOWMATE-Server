package com.wowmate.server.chatroom.service;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.chatroom.domain.CreateChatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.repository.ChatroomRepository;
import com.wowmate.server.chatroom.repository.CreateChatroomRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    private final CreateChatroomRepository createChatroomRepository;
    private final UserRepository userRepository;

    private final PostRepository postRepository;


    // 채팅방 목록 조회
    public List<GetChatroomListDto> getChatroomList(User user) {

        if (user == null) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        List<GetChatroomListDto> chatroomListDtos = chatroomRepository
                .findByUserId(user.getId())
                .stream()
                .map(
                        chatroom ->
                                GetChatroomListDto.builder()
                                        .postTitle(chatroom.getCreateChatroom().getPost().getTitle())
                                        // .lastMessage(chatroom.getMessages().get(chatroom.getMessages().size() - 1).getContent())
                                        // .lastMessageDate(chatroom.getMessages().get(chatroom.getMessages().size() - 1).getCreatedDate())
                                        .build()
                )
                .collect(Collectors.toList());

        return chatroomListDtos;

    }


    // 특정 채팅방 조회
    public GetChatroomDto getChatroom(Long chatroomId, User user) {

        if (user == null) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Chatroom chatroom = chatroomRepository
                .findByChatroomIdAndUserId(chatroomId,user.getId());

        GetChatroomDto chatroomDto = GetChatroomDto
                .builder()
                .postTitle(chatroom.getCreateChatroom().getPost().getTitle())
                .createDate(chatroom.getCreatedDate())
                .messageList(chatroom.getMessages())
                .opponentImg(userRepository.findById(chatroom.getOpponentUserId()).get().getImage())
                .postCategory(chatroom.getCreateChatroom().getPost().getCategory().getName())
                .build();

        return chatroomDto;


    }


    // 채팅방 삭제
    public void deleteChatroom(Long chatroomId, User user) {

        // createChatroom에서 삭제해야하나?...흠
        try {
            Chatroom chatroom = chatroomRepository.findById(chatroomId).get();
            chatroomRepository.delete(chatroom);
        } catch (Exception e) {
            throw new IllegalStateException("존재하지 않는 채팅방입니다.");
        }

    }


    // 채팅방 만들기
    public GetChatroomDto createChatroom(Long postId, User user) {
        Post post = postRepository.findById(postId).get();

        CreateChatroom createChatroom = new CreateChatroom(post, user);
        createChatroomRepository.save(createChatroom);

        Chatroom chatroomForUser = Chatroom.createChatroomForUser(createChatroom);
        Chatroom chatroomForPostUser = Chatroom.createChatroomForPostUser(createChatroom);

        chatroomRepository.save(chatroomForUser);
        chatroomRepository.save(chatroomForPostUser);

        GetChatroomDto getChatroomDto = this.getChatroom(chatroomForUser.getId(), user);

        return getChatroomDto;
    }

}

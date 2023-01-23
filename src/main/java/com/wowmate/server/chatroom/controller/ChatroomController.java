package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.domain.CreateChatroom;
import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.service.ChatroomService;
import com.wowmate.server.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Chatroom", description = "채팅방 API")
// @RequestMapping(value = "")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @GetMapping
    public List<GetChatroomListDto> getChatroomList(User user) {
        // @AuthenticationPrincipal 현재 접속한 유저 확인
        return chatroomService.getChatroomList(user);

    }

    @GetMapping(value = "/chats/{chatId}")
    public GetChatroomDto getChatroom(@PathVariable("chatId") Long chatroomId, User user) {

        return chatroomService.getChatroom(chatroomId, user);

    }

    @DeleteMapping(value = "/chats/{chatId}")
    public void deleteChatroom(@PathVariable("chatId") Long chatroomId, User user) {

        chatroomService.deleteChatroom(chatroomId, user);
        // 리다이렉트 시켜줘야하나?
    }

    // 채팅방을 만들면 chats/{chatId}로 가야하나?
    // 그리고 메세지를 보내야만 채팅방이 만들어지는 것? 흠.. 헷갈리네 get인가?
    @PostMapping(value = "posts/{postId}/chat/create")
    public GetChatroomDto createChatroom(@PathVariable Long postId, User user) {

        return chatroomService.createChatroom(postId, user);

    }
}

package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.dto.GetChatroomDto;
import com.wowmate.server.chatroom.dto.GetChatroomListDto;
import com.wowmate.server.chatroom.service.ChatroomService;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.Response;
import com.wowmate.server.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chatroom", description = "채팅방 API")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @Operation(tags = "Chatroom", description = "채팅방 목록 조회")
    @GetMapping(value = "/chats")
    public Response<List<GetChatroomListDto>, Object> getChatroomList(@AuthenticationPrincipal User user) {

        try {

            List<GetChatroomListDto> chatroomListDto = chatroomService.getChatroomList(user);
            return new Response<>(chatroomListDto);

        } catch(BaseException e) {

            return new Response<>(e.getResponseStatus());

        }

    }

    @Operation(tags = "Chatroom", description = "특정 채팅방 조회")
    @GetMapping(value = "/chats/{roomUuid}")
    public Response<GetChatroomDto, Object> getChatroom(@PathVariable("roomUuid") String roomUuid, @AuthenticationPrincipal User user) {

        try {
            GetChatroomDto chatroomDto = chatroomService.getChatroom(roomUuid, user);
            return new Response<>(chatroomDto);

        } catch(BaseException e) {

            return new Response<>(e.getResponseStatus());

        }

    }

    @Operation(tags = "Chatroom", description = "채팅방 삭제")
    @DeleteMapping(value = "/chats/{roomUuid}")
    public Response<List<GetChatroomListDto>, Object> deleteChatroom(@PathVariable("roomUuid") String roomUuid, @AuthenticationPrincipal User user) {

        try {
            List<GetChatroomListDto> chatroomListDto = chatroomService.deleteChatroom(roomUuid, user);
            return new Response<>(chatroomListDto);

        } catch(BaseException e) {

            return new Response<>(e.getResponseStatus());

        }

    }


     // 채팅을 보내야만 채팅방이 만들어지게 구현??
    @Operation(tags = "Chatroom", description = "채팅방 생성")
    @PostMapping(value = "/chat/create")
    public Response<GetChatroomDto, Object> createChatroom(@RequestParam Long postId, @AuthenticationPrincipal User user) {

        try {

            GetChatroomDto chatroomDto = chatroomService.createChatroom(postId, user);
            return new Response<>(chatroomDto);

        } catch (BaseException e) {

            return new Response<>(e.getResponseStatus());

        }

    }





}

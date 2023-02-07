package com.wowmate.server.chatroom.controller;

import com.wowmate.server.chatroom.service.MatchService;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.Response;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Match", description = "상대방 매칭")
public class MatchController {

    private final MatchService matchService;

    @MessageMapping(value = "/chats/{roomUuid}/match")
    public Response<Object, Object> matchRequest(@PathVariable("roomUuid") String roomUuid, @AuthenticationPrincipal User user) {

        try {

            matchService.matchRequest(roomUuid, user);
            return new Response<>(ResponseStatus.SUCCESS);

        } catch (BaseException e) {

            return new Response<>(e.getResponseStatus());

        }
    }
}

package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetChatroomListDto {

    private String postTitle;

    private String lastMessage;

    private LocalDateTime lastMessageDate;

}

package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class GetChatroomListDto {

    private String postTitle;

    private String lastMessage;

    private LocalDateTime lastMessageDate;

}

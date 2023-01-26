package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String chatroomUuid;
    private Long postId;
//    private Long senderId;
    private MessageType messageType;
    private String content;

}

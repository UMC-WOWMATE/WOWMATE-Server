package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String message;
    private String sender;
    private String roomId;
    private Long senderId;
    private MessageType messageType;

}

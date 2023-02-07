package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.MessageType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private String chatroomUuid;
    private String senderEmail;
    private MessageType messageType;
    private String content;

}

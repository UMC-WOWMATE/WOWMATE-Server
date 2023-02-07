package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.MessageType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private String chatroomUuid;
    private Long postId;
    private String senderEmail;
    private String receiverEmail;
    private MessageType messageType;
    private String content;

}

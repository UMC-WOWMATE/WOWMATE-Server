package com.wowmate.server.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetMessageDto {

    private String content;
    private String senderEmail;
    private String sendTime;

}

package com.wowmate.server.chatroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wowmate.server.chatroom.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetMessageDto {

    private String content;

    private String senderEmail;

    private MessageType messageType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime sendTime;

}

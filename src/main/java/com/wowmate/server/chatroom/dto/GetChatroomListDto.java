package com.wowmate.server.chatroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetChatroomListDto {

    private String roomUuid;

    private String postTitle;

    private String lastMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime lastMessageDate;

    private String opponentUserImg;

}

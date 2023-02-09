package com.wowmate.server.chatroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetChatroomDto {

    private String postTitle;

    private String postCategory;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    private String userEmail;

    private String opponentEmail;

    private String opponentImg;

    private List<GetMessageDto> messageList;

}

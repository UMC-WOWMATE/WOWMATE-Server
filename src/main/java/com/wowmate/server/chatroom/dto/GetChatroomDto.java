package com.wowmate.server.chatroom.dto;

import lombok.*;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetChatroomDto {

    private String postTitle;
    private String postCategory;
    private String createdDate;
    private String opponentImg;
    private List<GetMessageDto> messageList;

}

package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetChatroomDto {

    private String opponentImg;
    private String postTitle;
    private String postCategory;
    private LocalDateTime createDate;
    private List<Message> messageList;

}

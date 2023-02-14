package com.wowmate.server.chatroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wowmate.server.chatroom.domain.MessageType;
import com.wowmate.server.user.domain.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MatchMessageDto {

    private String senderEmail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendTime;

    private String content;

    private Gender gender;

    private String phoneNumber;

    private int age;

    private MessageType messageType;

}

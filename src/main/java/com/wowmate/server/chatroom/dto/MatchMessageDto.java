package com.wowmate.server.chatroom.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class MatchMessageDto {

    private String gender;
    private String School;
    private Long phoneNumber;
    private int age;

}

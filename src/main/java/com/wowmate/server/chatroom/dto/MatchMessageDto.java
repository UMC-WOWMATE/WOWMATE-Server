package com.wowmate.server.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class MatchMessageDto {

    private String gender;
    private String phoneNumber;
    private int age;

}

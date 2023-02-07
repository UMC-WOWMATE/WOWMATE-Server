package com.wowmate.server.chatroom.domain;

import com.wowmate.server.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchInfo {

    private Gender gender;

    private int age;

    private String phoneNumber;

}

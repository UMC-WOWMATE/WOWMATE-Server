package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChatroom extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_chatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String opponentUserEmail;

    private String opponentUserImg;

    //== 연관관계 메서드==//
//    public List<UserChatroom> createUserChatroom(Chatroom chatroom) {
//
//        UserChatroom requestUserChatroom = UserChatroom.builder()
//                .chatroom(chatroom)
//                .user(chatroom.getRequestUser())
//                .opponentUserImg(chatroom.getPost().getUser().getImage())
//                .build();
//
//        UserChatroom postUserChatroom = UserChatroom.builder()
//                .chatroom(chatroom)
//                .user(chatroom.getPost().getUser())
//                .opponentUserImg(chatroom.getRequestUser().getImage())
//                .build();
//
//
//    }


}

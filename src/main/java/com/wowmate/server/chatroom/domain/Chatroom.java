package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Chatroom extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    @ManyToOne(fetch  = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_chatroom_id")
    private CreateChatroom createChatroom;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Message> messages;

    private Long userId;
    private Long opponentUserId;

    public static Chatroom createChatroomForUser(CreateChatroom createChatroom) {
        Chatroom chatroom = new Chatroom();
        chatroom.setCreateChatroom(createChatroom);
        chatroom.setUserId(createChatroom.getUser().getId());
        chatroom.setOpponentUserId(createChatroom.getPostUserId());

        return chatroom;
    }

    public static Chatroom createChatroomForPostUser(CreateChatroom createChatroom) {
        Chatroom chatroom = new Chatroom();
        chatroom.setCreateChatroom(createChatroom);
        chatroom.setUserId(createChatroom.getPostUserId());
        chatroom.setOpponentUserId(createChatroom.getUser().getId());

        return chatroom;
    }

}

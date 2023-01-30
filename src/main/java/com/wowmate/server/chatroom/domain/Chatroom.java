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

    @Column(name = "chatroom_uuid")
    private String uuid;

    @ManyToOne(fetch  = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_chatroom_id")
    private CreateChatroom createChatroom;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Message> messages;

    private String userEmail;
    private String opponentUserEmail;

    public static Chatroom createChatroomForUser(CreateChatroom createChatroom) {
        Chatroom chatroom = new Chatroom();
        chatroom.setCreateChatroom(createChatroom);
        chatroom.setUserEmail(createChatroom.getUser().getEmail());
        chatroom.setOpponentUserEmail(createChatroom.getPostUserEmail());
        chatroom.setUuid(createChatroom.getUuid());

        return chatroom;
    }

    public static Chatroom createChatroomForPostUser(CreateChatroom createChatroom) {
        Chatroom chatroom = new Chatroom();
        chatroom.setCreateChatroom(createChatroom);
        chatroom.setUserEmail(createChatroom.getPostUserEmail());
        chatroom.setOpponentUserEmail(createChatroom.getUser().getEmail());
        chatroom.setUuid(createChatroom.getUuid());

        return chatroom;
    }

}

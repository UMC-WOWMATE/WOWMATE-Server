package server.chatroom.domain;

import lombok.Getter;
import lombok.Setter;
import server.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Message extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatRoom;

    private Long sendUserId;
    private String content;

}

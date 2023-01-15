package chatroom.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Chatroom {

    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_chatroom_id")
    private CreateChatroom createChatroom;

    private Long postId;
    private Long writeUserId;
    private Long replyUserId;

}

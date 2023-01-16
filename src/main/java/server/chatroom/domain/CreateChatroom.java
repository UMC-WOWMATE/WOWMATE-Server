package server.chatroom.domain;

import lombok.Getter;
import lombok.Setter;
import server.BaseEntity;
import server.post.domain.Post;
import server.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CreateChatroom extends BaseEntity  {

    @Id
    @GeneratedValue
    @Column(name = "createchatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "reply_user_id")
    private User user;

    private Long writeUserId;


}

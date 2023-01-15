package chatroom.domain;

import lombok.Getter;
import lombok.Setter;
import post.domain.Post;
import user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CreateChatroom {

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

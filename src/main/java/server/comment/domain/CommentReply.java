package server.comment.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import server.BaseEntity;
import server.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class CommentReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private int likeNumber;

}

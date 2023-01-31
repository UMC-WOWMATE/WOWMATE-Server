package com.wowmate.server.comment.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String context;

    private int commentReplyLikeNumber;
    public CommentReply(Comment comment, User user, String context, int commentReplyLikeNumber) {
        this.comment = comment;
        this.user = user;
        this.context = context;
        this.commentReplyLikeNumber = commentReplyLikeNumber;
    }

}

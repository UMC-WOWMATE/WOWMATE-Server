package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CreateChatroom extends BaseEntity  {

    @Id
    @GeneratedValue
    @Column(name = "create_chatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "reply_user_id")
    private User user;

    private Long writeUserId;

}

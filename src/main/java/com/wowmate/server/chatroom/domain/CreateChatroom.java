package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreateChatroom extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "create_chatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createChatroom")
    private List<Chatroom> chatrooms = new ArrayList<>();

    private Long postUserId;

    //== 연관 관계 메서드==//
    public CreateChatroom(Post post, User user) {
        this.post = post;
        this.user = user;
        this.postUserId = post.getUser().getId();
    }

}

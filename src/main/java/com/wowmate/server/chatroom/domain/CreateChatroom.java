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
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreateChatroom extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "chatroom_uuid", unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "createChatroom")
    private List<Chatroom> chatrooms = new ArrayList<>();


    private String postUserEmail;

    //== 연관 관계 메서드==//
    public CreateChatroom(Post post, User user) {
        this.post = post;
        this.user = user;
        this.postUserEmail = post.getUser().getEmail();
    }

}

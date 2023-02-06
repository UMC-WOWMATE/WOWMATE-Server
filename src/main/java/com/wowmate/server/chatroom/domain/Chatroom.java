package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Chatroom extends BaseEntity {

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
    @JoinColumn(name = "request_user_id")
    private User requestUser;

    @OneToMany(mappedBy = "chatroom")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom")
    private List<UserChatroom> userChatrooms = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    private String postUserEmail;


    //== 생성자 ==//
    public Chatroom(Post post, User requestUser) {

        this.post = post;
        this.requestUser = requestUser;
        this.postUserEmail = post.getUser().getEmail();
        this.uuid = UUID.randomUUID().toString();
        this.matchType = MatchType.YET;

    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

}

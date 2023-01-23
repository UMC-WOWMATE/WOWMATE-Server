package com.wowmate.server.post.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String context;
    private int likeNumber;
    private int hits;

    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

//    private image1;
//    private image2;
//    private image3;
//    private image4;
//    private image5;


}

package com.wowmate.server.post.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.user.domain.User;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @ManyToOne
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

    public Post(Category category, String title, String context, int likeNumber) {
        this.category = category;
        this.title = title;
        this.context = context;
        this.likeNumber = likeNumber;
    }

    public Post(String title, String context, int likeNumber) {
        this.title = title;
        this.context = context;
        this.likeNumber = likeNumber;
    }
}

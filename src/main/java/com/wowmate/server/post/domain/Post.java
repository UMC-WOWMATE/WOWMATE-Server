package com.wowmate.server.post.domain;

import com.wowmate.server.BaseEntity;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    
    private String title;
    private String context;
    private int likeNumber;
    private String categoryName;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    private int member;

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    public Post(User user, String title, String context, String categoryName, String tag1, String tag2, String tag3, String tag4, String tag5, int member,
                String image1, String image2, String image3, String image4, String image5) {
        this.user = user;
        this.title = title;
        this.context = context;
        this.categoryName = categoryName;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        this.member = member;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
    }
}

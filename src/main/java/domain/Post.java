package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<Comment>();

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

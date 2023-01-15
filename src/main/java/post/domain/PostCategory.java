package post.domain;

import lombok.Getter;
import lombok.Setter;
import post.domain.Category;
import post.domain.Post;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PostCategory {

    @Id
    @GeneratedValue
    @Column(name = "postcategory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "category_id")
    private Category category;

}

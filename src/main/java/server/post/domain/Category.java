package server.post.domain;

import lombok.Getter;
import lombok.Setter;
import server.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<PostCategory> postCategoryList = new ArrayList<PostCategory>();


}

package com.wowmate.server.post.domain;

import com.wowmate.server.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PostCategory extends BaseEntity {

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

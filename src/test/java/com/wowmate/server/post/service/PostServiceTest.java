package com.wowmate.server.post.service;

import com.wowmate.server.comment.repository.CommentReplyRepository;
import com.wowmate.server.comment.repository.CommentRepository;
import com.wowmate.server.post.domain.Category;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.CategoryRepository;
import com.wowmate.server.post.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Rollback(value = false)
@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class PostServiceTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void getAllPostList() {
          /*Category c = new Category("운동");
        categoryRepository.save(c);*/

        Category category = new Category("운동");
        categoryRepository.save(category);
        Post post1 = new Post(category,"ㅎ", "ㅇ", 3);
        postRepository.save(post1);

    }

    @Test
    public void getPostListByTitle() {
    }

    @Test
    public void getPostClick() {
    }

    @Test
    public void getCommentList() {
    }

    @Test
    public void testGetAllPostList() {
    }
}
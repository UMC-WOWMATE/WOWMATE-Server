package com.wowmate.server.post.repository;

import com.wowmate.server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String postTitle);

    List<Post> findByCategoryName(String categoryName);

}

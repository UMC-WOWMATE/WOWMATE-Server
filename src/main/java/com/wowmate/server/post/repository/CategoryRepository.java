package com.wowmate.server.post.repository;

import com.wowmate.server.post.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

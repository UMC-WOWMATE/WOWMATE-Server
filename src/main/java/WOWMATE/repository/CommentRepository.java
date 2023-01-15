package WOWMATE.repository;

import WOWMATE.domain.Comment;
import WOWMATE.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}


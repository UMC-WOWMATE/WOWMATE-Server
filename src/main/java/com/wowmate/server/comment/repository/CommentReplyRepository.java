package com.wowmate.server.comment.repository;

import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.comment.domain.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {

    List<CommentReply> findAllByCommentId(Long CommentId);
}

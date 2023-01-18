package com.wowmate.server.comment.repository;

import com.wowmate.server.comment.domain.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
}

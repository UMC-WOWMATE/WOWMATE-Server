package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query("select c from Chatroom c where c.post.id = :postId " +
            "and c.requestUser.email = :requestUserEmail")
    Optional<Chatroom> findByPostIdAAndRequestUserEmail
            (@Param("postId") Long postId, @Param("requestUserEmail") String requestUserEmail);

    @Query("select c from Chatroom c where c.uuid = :chatroomUuid")
    Optional<Chatroom> findByUuid(@Param("chatroomUuid") String chatroomUuid);

    @Query("select c from Chatroom c where c.post.id = :postId")
    List<Chatroom> findByPostId(@Param("postId") Long postId);

}

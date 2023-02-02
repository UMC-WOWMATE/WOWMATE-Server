package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query("select c from Chatroom c where c.userId = :userId")
    List<Chatroom> findByUserId(@Param("userId") Long userId);

    @Query("select c from Chatroom c where c.id = :chatroomId and c.userId = :userId")
    Chatroom findByChatroomIdAndUserId(@Param("chatroomId")Long chatroomId, @Param("userId")Long userId);

    @Query("select c from Chatroom c where c.uuid = :chatroomUuid and c.userId = :userId")
    Chatroom findByChatroomUuidAndUserId(@Param("chatroomUuid")String chatroomId, @Param("userId")Long userId);

    @Query("select c from Chatroom c where c.uuid = :chatroomUuid")
    Optional<Chatroom> findByUuid(@Param("chatroomUuid") String chatroomUuid);

}

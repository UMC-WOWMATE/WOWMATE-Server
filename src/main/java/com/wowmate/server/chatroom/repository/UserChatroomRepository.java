package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.UserChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {

    @Query("select uc from UserChatroom uc join Chatroom c where uc.user.email = :email and c.uuid = :uuid")
    Optional<UserChatroom> findByUuidAndEmail(@Param("uuid") String uuid, @Param("email") String email);
}

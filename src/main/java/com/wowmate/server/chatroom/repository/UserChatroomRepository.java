package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.UserChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {

    @Query("select uc from UserChatroom uc where uc.user.email = :email and uc.chatroom.uuid = :uuid")
    Optional<UserChatroom> findByUuidAndEmail(@Param("uuid") String uuid, @Param("email") String email);

    @Query("select uc from UserChatroom uc where uc.user.email = :email")
    List<UserChatroom> findByUserEmail(@Param("email") String email);

}

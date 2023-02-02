package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.CreateChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateChatroomRepository extends JpaRepository<CreateChatroom, Long> {

}

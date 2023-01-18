package com.wowmate.server.chatroom.repository;

import com.wowmate.server.chatroom.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>  {
}

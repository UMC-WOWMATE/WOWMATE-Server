package server.chatroom.repository;

import server.chatroom.domain.CreateChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateChatroomRepositroy extends JpaRepository<CreateChatroom, Long> {
}

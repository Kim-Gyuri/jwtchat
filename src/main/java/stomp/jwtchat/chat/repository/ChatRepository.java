package stomp.jwtchat.chat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import stomp.jwtchat.chat.entity.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByRoomId(Long roomId);
}
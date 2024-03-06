package stomp.jwtchat.room.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import stomp.jwtchat.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
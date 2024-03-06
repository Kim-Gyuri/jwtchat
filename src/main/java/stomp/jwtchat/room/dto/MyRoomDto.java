package stomp.jwtchat.room.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stomp.jwtchat.room.entity.Room;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyRoomDto {
    private Long roomId;
    private String name;

    public static MyRoomDto from(Room room) {
        MyRoomDto myRoomDto = new MyRoomDto();
        myRoomDto.setRoomId(room.getId());
        myRoomDto.setName(room.getName());
        return myRoomDto;
    }
}
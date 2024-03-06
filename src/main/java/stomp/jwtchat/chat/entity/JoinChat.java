package stomp.jwtchat.chat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stomp.jwtchat.member.entity.Member;
import stomp.jwtchat.room.entity.Room;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinChat {
    @Id
    @GeneratedValue
    @Column(name = "join_chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
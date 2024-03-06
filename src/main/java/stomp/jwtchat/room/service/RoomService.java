package stomp.jwtchat.room.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.JoinColumn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stomp.jwtchat.chat.entity.JoinChat;
import stomp.jwtchat.chat.repository.JoinChatRepository;
import stomp.jwtchat.member.entity.Member;
import stomp.jwtchat.member.repository.MemberRepository;
import stomp.jwtchat.room.dto.MyRoomDto;
import stomp.jwtchat.room.dto.RoomFormDto;
import stomp.jwtchat.room.entity.Room;
import stomp.jwtchat.room.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final JoinChatRepository joinChatRepository;

    /**
     * 채팅방 생성
     */
    public void createRoom(String loginId, RoomFormDto form) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException());

        Room roomEntity = Room.builder()
                .name(form.getName())
                .build();

        JoinChat joinChat = JoinChat.builder()
                .member(member)
                .room(roomEntity)
                .build();
        roomRepository.save(roomEntity).getId();
        joinChatRepository.save(joinChat);
        log.info("[RoomService-createRoom] room, joinChat 엔티티 저장");
    }

    /**
     * 모든 채팅방 찾기
     */
    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    public List<MyRoomDto> findMyRoom(String loginId) {
        return joinChatRepository.findByMemberId(loginId).stream()
                .map(joinChat -> MyRoomDto.from(joinChat.getRoom()))
                .collect(Collectors.toList());
    }

    /**
     * 들어간 채팅방 저장
     */
    public void saveRoom(String loginId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException());
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException());

        JoinChat joinChat = JoinChat.builder()
                .member(member)
                .room(room)
                .build();
        Optional<JoinChat> joinChatPresent = joinChatRepository.findByMemberIdAndRoomId(member.getLoginId(), room.getId());
        if (joinChatPresent.isEmpty()) {
            log.info("회원 채팅방 목록에 추가");
            joinChatRepository.save(joinChat);
        }
    }
}
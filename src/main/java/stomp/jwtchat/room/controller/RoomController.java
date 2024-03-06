package stomp.jwtchat.room.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import stomp.jwtchat.chat.dto.ChatDto;
import stomp.jwtchat.chat.service.ChatService;
import stomp.jwtchat.room.dto.MyRoomDto;
import stomp.jwtchat.room.dto.RoomFormDto;
import stomp.jwtchat.room.entity.Room;
import stomp.jwtchat.room.service.RoomService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomService roomService;
    private final ChatService chatService;

    /**
     * 채팅방 입장
     */
    @GetMapping("/{roomId}")
    public String joinRoom(@PathVariable(required = false) Long roomId, Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("user");
        roomService.saveRoom(loginId, roomId);
        List<ChatDto> chatList = chatService.findAllChatByRoomId(roomId);

        model.addAttribute("roomId", roomId);
        model.addAttribute("chatList", chatList);
        return "chat/roomIn";
    }

    /**
     * 채팅방 등록
     */
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<?> createRoom(RoomFormDto form, HttpSession session) {
        String loginId = (String) session.getAttribute("user");
        roomService.createRoom(loginId, form);
        return ResponseEntity.ok(null);
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public String roomList(Model model) {
        List<Room> roomList = roomService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/roomList";
    }

    /**
     * 내가 들어간 채팅방 리스트
     */
    @GetMapping("/my-chat-list")
    public String myChatList(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("user");
        List<MyRoomDto> myRoomDtoList = roomService.findMyRoom(loginId);
        model.addAttribute("myRoomDtoList", myRoomDtoList);
        return "chat/myRoom";
    }

    /**
     * 방만들기 폼
     */
    @GetMapping("/roomForm")
    public String roomForm() {
        return "chat/roomForm";
    }
}
package stomp.jwtchat.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import stomp.jwtchat.chat.dto.ChatDto;
import stomp.jwtchat.chat.service.ChatService;
import stomp.jwtchat.security.filter.JwtTokenProvider;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}") // 여길 구독하고 있는 곳으로 메시지 전송
    public ChatDto messageHandler(@DestinationVariable("roomId") Long roomId, ChatDto message, @Header("token") String token) {
        String loginId = jwtTokenProvider.getUserNameFromJwt(token);//loginId 가져옴
        return chatService.createChat(roomId, message.getMessage(), loginId);
    }
}
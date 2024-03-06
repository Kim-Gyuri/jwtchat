package stomp.jwtchat.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String name; // 로그인한 회원
}
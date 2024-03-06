package stomp.jwtchat.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import stomp.jwtchat.member.dto.JoinFormDto;
import stomp.jwtchat.member.dto.LoginFormDto;
import stomp.jwtchat.member.service.MemberService;
import stomp.jwtchat.security.dto.TokenDto;
import stomp.jwtchat.security.filter.JwtTokenProvider;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginFormDto loginFormDto, HttpSession session) {
        TokenDto tokenDto = memberService.login(loginFormDto.getLoginId(), loginFormDto.getPassword());

        String loginId = jwtTokenProvider.getUserNameFromJwt(tokenDto.getAccessToken());
        session.setAttribute("user", loginId);

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/join")
    public String joinForm() {
        return "member/register";
    }

    @PostMapping("/join")
    public String saveMember(JoinFormDto joinFormDto) {
        memberService.saveMember(joinFormDto);
        return "redirect:/login";
    }
}
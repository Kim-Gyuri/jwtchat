package stomp.jwtchat.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stomp.jwtchat.member.dto.JoinFormDto;
import stomp.jwtchat.member.entity.Member;
import stomp.jwtchat.member.repository.MemberRepository;
import stomp.jwtchat.security.dto.TokenDto;
import stomp.jwtchat.security.filter.JwtTokenProvider;

import java.util.Collections;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;

    /**
     * 회원가입
     */
    public Long saveMember(JoinFormDto joinFormDto) {

        //중복체크
        Optional<Member> existId = memberRepository.findByLoginId(joinFormDto.getLoginId());
        Optional<Member> existName = memberRepository.findByName(joinFormDto.getName());
        if(!existId.isEmpty() || !existName.isEmpty()) throw new IllegalStateException("이미 존재하는 회원입니다.");

        Member member = Member.builder()
                .loginId(joinFormDto.getLoginId())
                .password(encoder.encode(joinFormDto.getPassword()))
                .name(joinFormDto.getName())
                .roles(Collections.singletonList("ROLE_USER")) // 기본 권한 설정
                .build();
        return memberRepository.save(member).getId();
    }

    public TokenDto login(String memberId, String password) {
        log.info("login 프로세스 진입");
        System.out.println(memberId);
        System.out.println(password);
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        return tokenDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        return member;
    }
}
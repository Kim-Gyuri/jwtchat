package stomp.jwtchat.chat;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stomp.jwtchat.member.entity.Member;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initMember();
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class InitService {
        private final EntityManager em;
        private final PasswordEncoder encoder;

        public void initMember() {
            Member member1 = Member.builder().loginId("a").password(encoder.encode("a")).roles(Collections.singletonList("ROLE_USER")).name("사용자A").build();
            Member member2 = Member.builder().loginId("b").password(encoder.encode("b")).roles(Collections.singletonList("ROLE_USER")).name("사용자B").build();

            em.persist(member1);em.persist(member2);

        }
    }
}
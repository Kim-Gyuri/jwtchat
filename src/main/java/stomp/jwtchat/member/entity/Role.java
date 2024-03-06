package stomp.jwtchat.member.entity;

public enum Role {
    ROLE_MEMBER("일반사용자"), ROLE_ADMIN("관리자");

    private String description;

    Role(String description) {
        this.description = description;
    }
}
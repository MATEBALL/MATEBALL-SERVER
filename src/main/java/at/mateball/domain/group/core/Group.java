package at.mateball.domain.group.core;

import at.mateball.domain.game_information.core.GameInformation;
import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "match_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // TODO: user,leader 네이밍 고민

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_information_id", nullable = false, unique = true)
    private GameInformation gameInformation;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer status = 1; // TODO: "대기중"==1 로 기본값 설정함. Integer 값 확정 필요

    @Column(length = 1000)
    private String chattingUrl;

    protected Group() {
    }

    public Group(User user, GameInformation gameInformation, LocalDateTime createdAt, Integer status) {
        this.user = user;
        this.gameInformation = gameInformation;
        this.createdAt = createdAt;
        this.status = status;
    }
}

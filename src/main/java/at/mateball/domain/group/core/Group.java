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
    private User leader;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_information_id", nullable = false)
    private GameInformation gameInformation;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private int status = 1; // TODO: "대기중"==1 로 기본값 설정함. 값 확정 필요

    @Column(length = 1000)
    private String chattingUrl;

    protected Group() {
    }

    public Group(User leader, GameInformation gameInformation, LocalDateTime createdAt, int status) {
        this.leader = leader;
        this.gameInformation = gameInformation;
        this.createdAt = createdAt;
        this.status = status;
    }
}

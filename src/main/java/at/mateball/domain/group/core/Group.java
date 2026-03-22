package at.mateball.domain.group.core;

import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "match_group",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "game_information_id"}),
                @UniqueConstraint(name = "uk_group_chatting", columnNames = {"chatting_id"})
        }
)
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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chatting_id", nullable = true)
    private Chatting chatting;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private boolean isGroup;

    protected Group() {
    }

    public Group(User leader, GameInformation gameInformation, LocalDateTime createdAt, int status, boolean isGroup) {
        this.leader = leader;
        this.gameInformation = gameInformation;
        this.createdAt = createdAt;
        this.status = status;
        this.isGroup = isGroup;
    }

    public static Group create(User leader, GameInformation gameInformation, boolean isGroup) {
        return new Group(leader, gameInformation, LocalDateTime.now(), GroupStatus.PENDING.getValue(), isGroup);
    }

    public void assignChatting(Chatting chatting) {
        this.chatting = chatting;
    }
}

package at.mateball.domain.group_member.core;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "group_member")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private Boolean isParticipant = false;

    @Column(nullable = true)
    private int status = 3; // TODO: "승인 대기 중"==3 로 기본값 설정함. 값 확정 필요

    protected GroupMember() {
    }

    public GroupMember(User user, Group group, Boolean isParticipant, int status) {
        this.user = user;
        this.group = group;
        this.isParticipant = isParticipant;
        this.status = status;
    }
}

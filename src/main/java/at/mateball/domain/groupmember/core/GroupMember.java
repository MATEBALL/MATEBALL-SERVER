package at.mateball.domain.groupmember.core;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "group_member")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(nullable = false)
    private int status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_leader")
    private Boolean isLeader;

    protected GroupMember() {
    }

    public GroupMember(User user, Group group, Boolean isParticipant, int status) {
        this.user = user;
        this.group = group;
        this.isParticipant = isParticipant;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public GroupMember(User user, Group group, Boolean isParticipant, int status, boolean isLeader) {
        this.user = user;
        this.group = group;
        this.isParticipant = isParticipant;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.isLeader = isLeader;
    }
}

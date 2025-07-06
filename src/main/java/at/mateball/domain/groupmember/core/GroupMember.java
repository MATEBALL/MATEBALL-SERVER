package at.mateball.domain.groupmember.core;

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

    @Column(nullable = false)
    private int status;

    protected GroupMember() {
    }

    public GroupMember(User user, Group group, Boolean isParticipant, int status) {
        this.user = user;
        this.group = group;
        this.isParticipant = isParticipant;
        this.status = status;
    }
}

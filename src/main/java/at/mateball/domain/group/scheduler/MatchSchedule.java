package at.mateball.domain.group.scheduler;

import at.mateball.domain.group.core.Group;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "match_schedule")
public class MatchSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private LocalDateTime executeTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    @Column(nullable = false)
    private boolean executed = false;

    protected MatchSchedule() {
    }

    public MatchSchedule(Group group, LocalDateTime executeTime, ScheduleType type) {
        this.group = group;
        this.executeTime = executeTime;
        this.type = type;
        this.executed = false;
    }

    public void markExecuted() {
        this.executed = true;
    }
}

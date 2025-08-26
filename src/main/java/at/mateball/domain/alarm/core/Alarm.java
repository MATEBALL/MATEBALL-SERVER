package at.mateball.domain.alarm.core;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType type;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Alarm() {}

    public Alarm(User user, AlarmType type) {
        this.user = user;
        this.type = type;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}

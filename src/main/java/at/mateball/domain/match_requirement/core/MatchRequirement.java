package at.mateball.domain.match_requirement.core;

import at.mateball.domain.user.core.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "match_requirement")
public class MatchRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer team;

    @Column(nullable = true)
    private Integer teamAllowed;

    @Column(nullable = false)
    private Integer style;

    @Column(nullable = false)
    private Integer genderPreference;

    protected MatchRequirement() {
    }

    public MatchRequirement(User user, Integer team, Integer teamAllowed, Integer style, Integer genderPreference) {
        this.user = user;
        this.team = team;
        this.teamAllowed = teamAllowed;
        this.style = style;
        this.genderPreference = genderPreference;
    }
}

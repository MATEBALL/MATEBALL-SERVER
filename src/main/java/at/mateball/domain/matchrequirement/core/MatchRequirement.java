package at.mateball.domain.matchrequirement.core;

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

    @Column(nullable = true)
    private Integer team;

    @Column(nullable = true)
    private Integer teamAllowed;

    @Column(nullable = true)
    private Integer style;

    @Column(nullable = true)
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

    public void getMatchRequirement(){

    }

    public void updateAll(Integer team, Integer teamAllowed, Integer style, Integer genderPreference) {
        this.team = team;
        this.teamAllowed = teamAllowed;
        this.style = style;
        this.genderPreference = genderPreference;
    }
}

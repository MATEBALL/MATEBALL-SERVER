package at.mateball.domain.user.core;

import at.mateball.domain.match_requirement.core.MatchRequirement;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long kakaoUserId;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String birthyear;

    @Column(length = 45)
    private String nickname;

    @Column(length = 500)
    private String introduction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchRequirement> matchRequirements = new ArrayList<>();

    protected User() {

    }

    public User(Long kakaoUserId, String gender, String birthyear) {
        this.kakaoUserId = kakaoUserId;
        this.gender = gender;
        this.birthyear = birthyear;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateIntroduction(final String introduction) {
        this.introduction = introduction;
    }
}

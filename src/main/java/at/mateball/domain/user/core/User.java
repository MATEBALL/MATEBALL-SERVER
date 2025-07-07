package at.mateball.domain.user.core;

import at.mateball.domain.matchrequirement.core.MatchRequirement;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long kakaoUserId;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int birthYear;

    @Column(nullable = true, length = 45)
    private String nickname;

    @Column(nullable = true, length = 500)
    private String introduction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchRequirement> matchRequirements = new ArrayList<>();

    protected User() {

    }

    public User(Long kakaoUserId, String gender, int birthYear) {
        this.kakaoUserId = kakaoUserId;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateIntroduction(final String introduction) {
        this.introduction = introduction;
    }
}

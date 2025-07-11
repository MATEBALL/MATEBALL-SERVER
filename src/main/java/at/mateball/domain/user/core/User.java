package at.mateball.domain.user.core;

import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
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

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private int birthYear;

    @Column(nullable = true, length = 45)
    private String nickname;

    @Column(nullable = true, length = 500)
    private String introduction;

    @Column(nullable = true, length = 500)
    private String imgUrl;

    @Column(nullable = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchRequirement> matchRequirements = new ArrayList<>();

    protected User() {

    }

/*    public User(Long kakaoUserId, String gender, int birthYear) {
        this.kakaoUserId = kakaoUserId;
        this.gender = gender;
        this.birthYear = birthYear;
    }*/

    public User(Long kakaoUserId, String email) {
        this.kakaoUserId = kakaoUserId;
        this.email = email;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateIntroduction(final String introduction) {
        this.introduction = introduction;
    }

    public void updateImgUrl(final String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void updateGenderAndBirthYear(Gender gender, int birthYear) {
        this.gender = gender.getRaw();
        this.birthYear = birthYear;
    }
}

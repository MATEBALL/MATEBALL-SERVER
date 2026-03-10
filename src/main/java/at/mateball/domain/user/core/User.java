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
    public static final String DEFAULT_PROFILE_IMAGE_URL =
            "https://mateball-file.s3.ap-northeast-2.amazonaws.com/profile.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long kakaoUserId;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private Integer birthYear;

    @Column(nullable = true, length = 45)
    private String nickname;

    @Column(nullable = true, length = 500)
    private String introduction;

    @Column(nullable = true, length = 500)
    private String imgUrl;

    @Column(name = "profile_image_key", length = 500)
    private String profileImageKey;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private boolean hasAccepted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchRequirement> matchRequirements = new ArrayList<>();

    @Column(nullable = true)
    private Integer avgSeason = 0;

    protected User() {

    }

    /*
    * imgUrl s3 안정화 되면 기본 이미지 URL 반환 로직 수정
    * this.profileImageKey = DEFAULT_PROFILE_IMAGE_KEY; 추가
    * 서비스에서 URL 상수를 직접 쓰는게 아니라 porfileImgKey 보고 URL 생성
    * */
    public User(Long kakaoUserId, String email, String imgUrl) {
        this.kakaoUserId = kakaoUserId;
        this.email = email;
        this.imgUrl = imgUrl;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(final String imgUrl) {
        this.imgUrl = (imgUrl != null) ? imgUrl : DEFAULT_PROFILE_IMAGE_URL;
    }

    public void updateProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }

    public void clearProfileImageKey() {
        this.profileImageKey = null;
    }

    public void updateIntroduction(final String introduction) {
        this.introduction = introduction;
    }

    public void updateGenderAndBirthYear(Gender gender, int birthYear) {
        this.gender = gender.getRaw();
        this.birthYear = birthYear;
    }

    public void updateHasAccepted(boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }
}

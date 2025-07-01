package at.mateball.domain.user.core;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long kakaoUserId;
    @Column
    private String gender;
    @Column
    private String birthyear;

    protected User() {

    }

    public User(Long kakaoUserId, String gender, String birthyear) {
        this.kakaoUserId = kakaoUserId;
        this.gender = gender;
        this.birthyear = birthyear;
    }
}

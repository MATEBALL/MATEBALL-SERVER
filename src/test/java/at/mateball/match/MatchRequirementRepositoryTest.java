package at.mateball.match;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.user.core.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MatchRequirementRepositoryTest {

    @Autowired
    private MatchRequirementRepository matchRequirementRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 매칭_점수_계산_정상작동_테스트() {
        // given
        User userA = new User(12345L, "M", 1997);
        entityManager.persist(userA);
        MatchRequirement reqA = new MatchRequirement(userA, 1, 1, 1, 3);
        entityManager.persist(reqA);

        User userB = new User(67890L, "F", 1998);
        entityManager.persist(userB);
        MatchRequirement reqB = new MatchRequirement(userB, 1, 1, 1, 3);
        entityManager.persist(reqB);

        entityManager.flush();
        entityManager.clear();

        // when
        List<MatchingScoreDto> result = matchRequirementRepository.findMatchingUsers(userA.getId());

        // then
        assertThat(result).hasSize(1);
        MatchingScoreDto score = result.get(0);

        System.out.println("[매칭 점수]: " +
                "teamScore = " + score.teamScore() + ", " +
                "genderScore = " + score.genderScore() + ", " +
                "styleScore = " + score.styleScore() + ", " +
                "total = " + score.totalScore());

        // 예상값: 팀(40), 성별(35), 스타일(25) = 총 100
        assertThat(score.totalScore()).isEqualTo(100);
    }
}


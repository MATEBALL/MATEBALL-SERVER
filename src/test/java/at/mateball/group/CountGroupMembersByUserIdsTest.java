package at.mateball.group;

import at.mateball.config.QuerydslConfig;
import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.infrastructure.dto.MemberMatchCountDto;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryImpl;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({QuerydslConfig.class, GroupV3RepositoryImpl.class})
class CountGroupMembersByUserIdsTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private GroupV3RepositoryImpl groupV3Repository;

    @Test
    void 함께한_매칭_수는_매칭완료_상태만_집계한다() {
        // given
        User leader = persistUser(1000L);
        User target = persistUser(2000L);

        // target 유저: MATCHED 2건 + 비-MATCHED(요청/실패 등) 3건
        em.persist(GroupMember.member(target, persistGroup(leader), GroupMemberStatus.MATCHED.getValue()));
        em.persist(GroupMember.member(target, persistGroup(leader), GroupMemberStatus.MATCHED.getValue()));
        em.persist(GroupMember.member(target, persistGroup(leader), GroupMemberStatus.PENDING_REQUEST.getValue()));
        em.persist(GroupMember.member(target, persistGroup(leader), GroupMemberStatus.AWAITING_APPROVAL.getValue()));
        em.persist(GroupMember.member(target, persistGroup(leader), GroupMemberStatus.MATCH_FAILED.getValue()));

        em.flush();
        em.clear();

        // when
        Map<Long, Integer> countMap = groupV3Repository
                .countGroupMembersByUserIds(List.of(target.getId()))
                .stream()
                .collect(Collectors.toMap(MemberMatchCountDto::memberId, MemberMatchCountDto::matchCount));

        // then: 전체 5건이 아니라 MATCHED 2건만 집계되어야 한다
        assertThat(countMap.getOrDefault(target.getId(), 0)).isEqualTo(2);
    }

    @Test
    void 매칭완료_내역이_없으면_결과에서_제외된다() {
        // given
        User leader = persistUser(1001L);
        User target = persistUser(2001L);

        em.persist(GroupMember.member(target, persistGroup(leader),
                GroupMemberStatus.PENDING_REQUEST.getValue()));

        em.flush();
        em.clear();

        // when
        List<MemberMatchCountDto> result = groupV3Repository.countGroupMembersByUserIds(List.of(target.getId()));

        // then: getOrDefault(..., 0) 으로 본인 프로필과 동일하게 0 으로 해석된다
        assertThat(result).isEmpty();
    }

    private User persistUser(long kakaoUserId) {
        User user = new User(kakaoUserId, "male", "http://example.com");
        em.persist(user);
        return user;
    }

    private Group persistGroup(User leader) {
        GameInformation game = new GameInformation(
                "두산", "LG", LocalDate.of(2026, 6, 13), LocalTime.of(18, 30), "잠실");
        em.persist(game);
        Group group = Group.create(leader, game, false);
        em.persist(group);
        return group;
    }
}

package at.mateball.domain.groupmember.core.repository;

import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.groupmember.core.repository.querydsl.GroupMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
    boolean existsByGroupIdAndUserIdAndStatus(Long matchId, Long userId, int status);
}

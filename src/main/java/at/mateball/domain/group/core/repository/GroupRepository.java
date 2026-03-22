package at.mateball.domain.group.core.repository;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.repository.querydsl.GroupRepositoryCustom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {
    /**
     * leader(User) 엔티티 내부의 id(user_id) 기준으로 삭제
     * 실제 SQL: delete from match_group where user_id = ?
     */
    @Modifying
    @Query("delete from Group g where g.leader.id = :userId")
    void deleteAllByLeaderId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from Group g where g.id = :groupId")
    Optional<Group> findGroupWithLock(Long groupId);
}

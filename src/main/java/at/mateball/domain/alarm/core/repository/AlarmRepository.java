package at.mateball.domain.alarm.core.repository;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.alarm.core.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserIdAndIsReadFalse(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Alarm a SET a.isRead = true " +
            "WHERE a.user.id = :userId AND a.type IN :types AND a.isRead = false")
    void markAsReadByUserAndTypes(@Param("userId") Long userId,
                                  @Param("types") List<AlarmType> types);
}

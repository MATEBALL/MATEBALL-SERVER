package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.infrastructure.dto.*;

import java.util.List;
import java.util.Optional;

public interface GroupV3RepositoryCustom {

    Optional<GameInfoQueryDto> findGameInfoByGameId(Long gameId);

    Optional<LoginUserMatchRequirementDto> findLoginUserMatchRequirement(Long userId);

    List<GroupMatchCandidateFlatDto> findMatchCandidatesByGameId(Long loginUserId, Long gameId);

    List<GroupMatchImageQueryDto> findMatchImagesByGameId(Long gameId);

    List<CreateGroupQueryDto> findCreateGroupsByUserId(Long userId);

    List<CreateGroupImageQueryDto> findCreateGroupImagesByMatchIds(List<Long> matchIds);
}
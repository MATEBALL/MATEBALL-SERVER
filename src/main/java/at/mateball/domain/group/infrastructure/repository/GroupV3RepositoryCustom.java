package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;

import java.util.List;
import java.util.Optional;

public interface GroupV3RepositoryCustom {

    Optional<GameInfoQueryDto> findGameInfoByGameId(Long gameId);

    Optional<LoginUserMatchRequirementDto> findLoginUserMatchRequirement(Long userId);

    List<GroupMatchCandidateFlatDto> findMatchCandidatesByGameId(Long loginUserId, Long gameId);
}
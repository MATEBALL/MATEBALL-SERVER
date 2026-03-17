package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.group.api.dto.MatchValidationRes;
import at.mateball.domain.group.infrastructure.dto.CreateGroupImageQueryDto;
import at.mateball.domain.group.infrastructure.dto.CreateGroupQueryDto;
import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchImageQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchMemberQueryDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.group.infrastructure.dto.MemberMatchCountDto;

import java.util.List;
import java.util.Optional;

public interface GroupV3RepositoryCustom {

    Optional<GameInfoQueryDto> findGameInfoByGameId(Long gameId);

    Optional<LoginUserMatchRequirementDto> findLoginUserMatchRequirement(Long userId);

    List<GroupMatchCandidateFlatDto> findMatchCandidatesByGameId(Long loginUserId, Long gameId);

    List<GroupMatchImageQueryDto> findMatchImagesByGameId(Long gameId);

    List<CreateGroupQueryDto> findCreateGroupsByUserId(Long userId);

    List<CreateGroupImageQueryDto> findCreateGroupImagesByMatchIds(List<Long> matchIds);

    Optional<Long> findLeaderIdByMatchId(Long matchId);

    List<GroupMatchMemberQueryDto> findMatchMembersByMatchId(Long matchId);

    List<MemberMatchCountDto> countGroupMembersByUserIds(List<Long> memberIds);

    List<RequestGroupQueryDto> findRequestGroupsByUserId(Long userId);

    List<GroupMatchImageQueryDto> findRequestGroupImagesByMatchIds(List<Long> matchIds);

    MatchValidationRes getMatchValidationInfo(Long userId, Long gameId);
}

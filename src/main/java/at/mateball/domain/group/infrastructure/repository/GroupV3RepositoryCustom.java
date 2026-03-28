package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.api.dto.MatchValidationRes;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.group.api.dto.RequestValidationRes;

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

    Optional<String> findLeaderNicknameByMatchId(Long matchId);

    List<GroupMatchMemberQueryDto> findMatchMembersByMatchId(Long matchId);

    List<MemberMatchCountDto> countGroupMembersByUserIds(List<Long> memberIds);

    List<RequestGroupQueryDto> findRequestGroupsByUserId(Long userId);

    List<GroupMatchImageQueryDto> findRequestGroupImagesByMatchIds(List<Long> matchIds);

    MatchValidationRes getMatchValidationInfo(Long userId, Long gameId);

    RequestValidationRes getValidation(Long userId, Long groupId);
}

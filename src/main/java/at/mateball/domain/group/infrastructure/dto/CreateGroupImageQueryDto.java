package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.group.core.assembler.MatchImageAssembler;

public record CreateGroupImageQueryDto(
        Long matchId,
        String profileImageKey
) implements MatchImageAssembler.MatchImageSource {
}

package at.mateball.domain.group.infrastructure.dto;

public record MemberMatchCountDto(
        Long memberId,
        Integer matchCount
) {
}

package at.mateball.domain.group.api.dto;

public record ChattingAccessRes(
        String chattingUrl,
        Long leaderId,
        Integer memberStatus
) {}

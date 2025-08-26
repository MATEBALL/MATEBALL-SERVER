package at.mateball.domain.chatting.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChattingRes(
        @Schema(description = "채팅방 주소")
        String chattingUrl
) {
}

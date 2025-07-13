package at.mateball.domain.groupmember.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupMemberCountRes(
        @NotNull
        @Schema(description = "매칭된 총 인원수")
        int count
) {
}

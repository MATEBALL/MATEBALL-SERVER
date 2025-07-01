package at.mateball.domain.sample.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SampleRequest(
        @NotNull
        @Schema(description = "어떤 필드인지 작성합니다")
        String data1,
        @Schema(description = "어떤 필드인지 작성합니다", nullable = true)
        String data2
) {
}

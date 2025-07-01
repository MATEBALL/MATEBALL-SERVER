package at.mateball.domain.sample.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record SampleRequest(
        @NotNull
        String data1,
        String data2
) {
}

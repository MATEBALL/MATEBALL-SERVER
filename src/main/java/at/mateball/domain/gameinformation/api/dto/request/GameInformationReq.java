package at.mateball.domain.gameinformation.api.dto.request;

import java.time.LocalDate;

public record GameInformationReq(
        LocalDate date
) {
}

package at.mateball.domain.sample.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.sample.api.dto.request.SampleRequest;
import at.mateball.domain.sample.api.dto.response.SampleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static at.mateball.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Tag(name = "api 집합의 이름을 작성합니다 ex)SAMPLE")
@RestController
@RequestMapping("/sample")
public class SampleController {

    public SampleController() {
    }

    @CustomExceptionDescription(SwaggerResponseDescription.SAMPLE_GET)
    @Operation(summary = "어떤 역할의 api인지 작성합니다", description = "api에 대한 세부 설명을 작성합니다")
    @GetMapping
    public MateballResponse<SampleResponse> getSampleData(
    ) {
        SampleResponse sampleData = new SampleResponse("다진언니 안녕");

        return MateballResponse.success(INTERNAL_SERVER_ERROR, sampleData);
    }

    @CustomExceptionDescription(SwaggerResponseDescription.SAMPLE_POST)
    @Operation(summary = "어떤 역할의 api인지 작성합니다", description = "api에 대한 세부 설명을 작성합니다")
    @PostMapping
    public MateballResponse<SampleResponse> postSampleData(
            @RequestBody final @Valid SampleRequest sampleRequest
    ) {
        SampleResponse sampleData = new SampleResponse(sampleRequest.data1());

        return MateballResponse.success(INTERNAL_SERVER_ERROR, sampleData);
    }
}

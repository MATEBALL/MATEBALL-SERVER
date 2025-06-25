package at.mateball.domain.sample.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.exception.ErrorCode;
import at.mateball.domain.sample.api.dto.response.SampleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    public SampleController() {
    }

    @GetMapping("/sample")
    public MateballResponse<SampleResponse> getSampleData() {
        SampleResponse sampleData = new SampleResponse("다진언니 안녕");

        return MateballResponse.success(ErrorCode.SUCCESS_SAMPLE, sampleData);
    }
}

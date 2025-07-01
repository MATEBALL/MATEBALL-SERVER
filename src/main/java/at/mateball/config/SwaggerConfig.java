package at.mateball.config;

import at.mateball.common.MateballResponse;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.ExampleHolder;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.exception.ErrorCode;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@OpenAPIDefinition(
        info = @Info(
                title = "MATEBALL",
                description = "메.친여자들의 Swagger에 오신 것을 환영합니다",
                version = "v1"
        )
)

@Configuration
public class SwaggerConfig {
    private final String securitySchemaName = "JWT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(createSecurityComponents())
                .addSecurityItem(createSecurityRequirement())
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 주소"), // TODO: 배포 후 삭제
                        new Server().url("https://api.mateball.co.kr").description("배포 주소"))
                );
    }

    private Components createSecurityComponents() {
        return new Components()
                .addSecuritySchemes(securitySchemaName, createBearerAuthScheme());
    }

    private SecurityScheme createBearerAuthScheme() {
        String securitySchemeName = "Bearer";
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(securitySchemeName)
                .bearerFormat(securitySchemaName)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement()
                .addList(securitySchemaName);
    }

    @Bean
    public OperationCustomizer customizeOperationWithExamples() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            CustomExceptionDescription customExceptionDescription = handlerMethod.getMethodAnnotation(
                    CustomExceptionDescription.class);

            if (customExceptionDescription != null) {
                generateErrorCodeResponseExample(operation, customExceptionDescription.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, SwaggerResponseDescription type) {

        ApiResponses responses = operation.getResponses();

        Set<ErrorCode> errorCodeList = type.getErrorCodeList();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                errorCodeList.stream()
                        .map(
                                errorCode -> {
                                    return ExampleHolder.builder()
                                            .holder(
                                                    createSwaggerErrorExample(errorCode))
                                            .code(errorCode.getStatus().value())
                                            .name(errorCode.toString())
                                            .build();
                                }
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example createSwaggerErrorExample(ErrorCode errorCode) {
        MateballResponse<Void> errorResponse = new MateballResponse<>(errorCode.getStatus().value(), errorCode.getMessage(), null);
        Example example = new Example();
        example.setValue(errorResponse);
        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }
}

package com.musa3team.devout.common.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "Authorization",
        description = "access Token을 입력해주세요.",
        in = SecuritySchemeIn.HEADER)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY)
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("Token을 입력해주세요.")
                        )
                )
                .info(apiInfo());
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] packages = {"com.musa3team.devout"};
        return GroupedOpenApi.builder()
                .group("springdoc-openapi")
                .packagesToScan(packages)
                .build();
    }

    private Info apiInfo() {
        String description = "고객에게 음식점 검색, 주문, 배달 현황 확인, 리뷰 등의 기능을 제공합니다. <br> " +
                                        "사장은 가게관리, 메뉴관리, 주문 관리를 할 수 있습니다.";
        return new Info()
                .title("음식 배달 api입니다.")
                .description(description)
                .version("2.5");
    }
}

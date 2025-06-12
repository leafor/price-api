package com.example.priceapi.infrastructure.adapter.in.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info().title("Price Service API").version("v1").description("Consulta de precios con prioridad y rangos de fecha"));
    }
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("prices").pathsToMatch("/api/prices/**").build();
    }

}

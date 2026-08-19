package com.unifiedhome.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI unifiedHomeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UnifiedHome API")
                        .version("1.0")
                        .description(
                                "REST API for managing smart-home rooms, " +
                                "devices and schedules."
                        ));
    }
    
}
package com.suhoi.demo.config;


import com.suhoi.demo.config.property.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title(openApiProperties.getTitle())
                        .version(openApiProperties.getVersion())
                        .description(openApiProperties.getDescription())
                        .contact(new Contact().name(openApiProperties.getContact().name())
                                .email(openApiProperties.getContact().email())))
                .servers(List.of(new Server().url(openApiProperties.getServer().url())
                        .description(openApiProperties.getServer().description())));
    }
}

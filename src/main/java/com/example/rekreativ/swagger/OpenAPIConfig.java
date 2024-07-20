package com.example.rekreativ.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Slf4j
public class OpenAPIConfig {
    private final BuildProperties buildProperties;

    public OpenAPIConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("build properties vers: " + buildProperties.getVersion());
        log.info("build properties: artf " + buildProperties.getArtifact());
        log.info("build properties: name " + buildProperties.getName());
        log.info("build properties: group " + buildProperties.getGroup());

        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                                    new io.swagger.v3.oas.models.security.SecurityScheme()
                                                            .name(securitySchemeName)
                                                            .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                                            .scheme("bearer")
                                                            .bearerFormat("JWT")
                                )
                )
                .info(new Info().title(buildProperties.getArtifact()).version(buildProperties.getVersion()));
    }
}

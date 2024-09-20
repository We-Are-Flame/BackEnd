package com.backend.before.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(List.of(securityRequirement));
    }

    @Bean
    public OpenApiCustomizer snakeCaseCustomiser() {
        return openApi -> openApi.getComponents().getSchemas().values().forEach(this::applySnakeCase);
    }

    private void applySnakeCase(Schema<?> schema) {
        Map<String, Schema> properties = schema.getProperties();
        if (properties != null) {
            Map<String, Schema> newProperties = new LinkedHashMap<>();
            for (Map.Entry<String, Schema> entry : properties.entrySet()) {
                String camelCase = entry.getKey();
                String snakeCase = camelToSnake(camelCase);
                newProperties.put(snakeCase, entry.getValue());
            }
            schema.setProperties(newProperties);
        }
    }

    private String camelToSnake(String str) {
        StringBuilder builder = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append('_');
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}

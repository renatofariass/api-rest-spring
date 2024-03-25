package com.api.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("RESTful API with Java and Spring Boot")
                        .version("v1")
                        .description("RESTful API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Renato Farias")
                                .url("https://www.linkedin.com/in/renatofari4s")
                        )
                );
    }
}

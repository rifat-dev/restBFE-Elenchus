package com.atom.forumEngine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Forum Engine").version("1.0.1")
                .description("REST API of the mini forum engine")
                .contact(new Contact().name("Rifat Murtazin")
                .url("https://github.com/rifat-dev/restBFE-Elenchus")));
    }
}

package dev.marvin.config;

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
                .info(new Info()
                        .title("Ecommerce API")
                        .version("1.0")
                        .description("Ecommerce API Documentation")
                        .contact(new Contact()
                                .email("marvin.nyingi97@gmail.com")
                                .name("Marvin Nyingi")
                        ));
    }
}

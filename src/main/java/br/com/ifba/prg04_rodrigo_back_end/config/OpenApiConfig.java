package br.com.ifba.prg04_rodrigo_back_end.config; // Se criar na pasta config

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Roteiro Livre")
                        .version("1.0")
                        .description("Documentação do sistema de agendamento de excursões")
                        .contact(new Contact()
                                .name("Rodrigo")
                                .email("almeidarodrigo201@gmail.com")));
    }
}
package br.com.poc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:4000").description("Servidor de Desenvolvimento")
                ))
                .info(new Info()
                        .title("API CRUD - Clientes e Pedidos")
                        .description("API REST para gerenciamento de clientes e pedidos com operações CRUD completas. " +
                                   "Esta API permite criar, listar, atualizar e deletar clientes e seus pedidos, " +
                                   "incluindo funcionalidades avançadas como busca por filtros, controle de status " +
                                   "de pedidos e relatórios.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("dev@empresa.com")
                                .url("https://empresa.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
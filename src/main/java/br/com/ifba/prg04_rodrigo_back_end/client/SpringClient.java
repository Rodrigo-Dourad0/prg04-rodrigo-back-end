package br.com.ifba.prg04_rodrigo_back_end.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class SpringClient {

    public static void main(String[] args) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String resposta = webClient.get()
                .uri("/usuarios")
                .retrieve()
                .bodyToMono(String.class)
                .block();


        System.out.println("--- RESPOSTA DA API ---");
        System.out.println(resposta);
    }
}
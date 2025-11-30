package br.com.ifba.prg04_rodrigo_back_end.viagem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ViagemCreateRequest {

    @NotBlank(message = "O título da viagem é obrigatório")
    private String titulo;

    @NotBlank(message = "O destino é obrigatório")
    private String destino;

    private String descricao;

    @NotNull(message = "Data de partida obrigatória")
    @Future(message = "A data de partida deve ser no futuro")
    private LocalDateTime dataPartida;

    @NotNull(message = "Data de retorno obrigatória")
    @Future(message = "A data de retorno deve ser no futuro")
    private LocalDateTime dataRetorno;

    @NotNull(message = "Preço obrigatório")
    private BigDecimal preco;

    @NotNull(message = "Total de vagas obrigatório")
    private Integer vagasTotais;


    @NotNull
    private Long organizadorId;
}
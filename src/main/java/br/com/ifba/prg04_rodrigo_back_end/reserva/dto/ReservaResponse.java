package br.com.ifba.prg04_rodrigo_back_end.reserva.dto;

import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusPagamento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaResponse {
    private Long id;
    private String nomeUsuario;
    private String tituloViagem;
    private Integer quantidadeLugares;
    private BigDecimal valorTotal; // Preço unitário x Quantidade
    private StatusPagamento status;
    private LocalDateTime dataReserva;
}
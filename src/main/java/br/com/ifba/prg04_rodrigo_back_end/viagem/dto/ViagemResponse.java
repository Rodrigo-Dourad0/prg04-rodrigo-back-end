package br.com.ifba.prg04_rodrigo_back_end.viagem.dto;

import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusViagem;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ViagemResponse {

    private Long id;

    private String titulo;

    private String destino;

    private String descricao;

    private LocalDateTime dataPartida;

    private LocalDateTime dataRetorno;

    private BigDecimal preco;

    private Integer vagasTotais;

    private StatusViagem status;

    private String nomeOrganizador;

    private String imagemUrl;

}
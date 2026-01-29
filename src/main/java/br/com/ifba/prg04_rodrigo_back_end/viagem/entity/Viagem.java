package br.com.ifba.prg04_rodrigo_back_end.viagem.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusViagem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_viagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viagem extends PersistenceEntity {

    private String titulo;
    private String destino;

    @Column(columnDefinition = "TEXT") // Permite textos longos, afim de descrever as excursões
    private String descricao;

    private LocalDateTime dataPartida;
    private LocalDateTime dataRetorno;

    private BigDecimal preco;
    private Integer vagasTotais;
    private String imagemUrl;

    @Enumerated(EnumType.STRING) // Salva no banco como texto ("ABERTA") em vez de número (0)
    private StatusViagem status = StatusViagem.ABERTA;

    // RELACIONAMENTO: Muitas Viagens pertencem a UM Organizador
    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private Organizador organizador;
}

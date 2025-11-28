package br.com.ifba.prg04_rodrigo_back_end.model.entity;

import br.com.ifba.prg04_rodrigo_back_end.model.enums.StatusViagem;
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
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String destino;

    @Column(columnDefinition = "TEXT") // Permite textos longos, afim de descrever as excursões
    private String descricao;

    private LocalDateTime dataPartida;
    private LocalDateTime dataRetorno;

    private BigDecimal preco;
    private Integer vagasTotais;

    @Enumerated(EnumType.STRING) // Salva no banco como texto ("ABERTA") em vez de número (0)
    private StatusViagem status = StatusViagem.ABERTA;

    // RELACIONAMENTO: Muitas Viagens pertencem a UM Organizador
    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private PerfilOrganizador organizador;
}

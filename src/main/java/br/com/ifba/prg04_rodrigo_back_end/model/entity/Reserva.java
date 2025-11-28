package br.com.ifba.prg04_rodrigo_back_end.model.entity;

import br.com.ifba.prg04_rodrigo_back_end.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataReserva = LocalDateTime.now(); // Pega a hora atual automaticamente
    private Integer quantidadeLugares;
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    // RELACIONAMENTO 1: Quem comprou?
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // RELACIONAMENTO 2: Qual viagem comprou?
    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;
}
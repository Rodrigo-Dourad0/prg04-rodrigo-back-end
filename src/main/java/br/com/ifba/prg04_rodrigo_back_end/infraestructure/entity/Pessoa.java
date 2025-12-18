package br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Table(name = "tb_pessoas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa extends PersistenceEntity {

    private String nome;

    @Column(unique = true)
    private String cpf;

    private String telefone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    @ToString.Exclude
    private Endereco endereco;
}
package br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_enderecos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends PersistenceEntity {

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String estado;

}
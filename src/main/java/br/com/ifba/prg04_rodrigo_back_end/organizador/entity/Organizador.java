package br.com.ifba.prg04_rodrigo_back_end.organizador.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_organizadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organizador extends PersistenceEntity {

    private String bio;
    private String chavePix;
    private String linkInstagram;
    private Boolean verificado = false;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
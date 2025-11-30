package br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tb_perfil_organizador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilOrganizador extends PersistenceEntity {


    private String bio; // Breve descrição da agência
    private String chavePix;
    private String linkInstagram;
    private Boolean verificado = false; // Começa como falso até o usuário se tornar organizador verificado

    @OneToOne
    @JoinColumn(name = "usuario_id") // Cria a chave estrangeira na tabela
    private Usuario usuario;
}
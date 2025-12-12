package br.com.ifba.prg04_rodrigo_back_end.usuario.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity.PerfilOrganizador;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tb_usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends PersistenceEntity {


    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;
    private String cpf;
    private String telefone;

    // Relacionamento: Um Usuário PODE TER um Perfil de Organizador
    // Cascade = Se deletar o usuario, deleta o perfil dele
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilOrganizador perfilOrganizador;

    public boolean isOrganizador() {
        return perfilOrganizador != null;
    }
}
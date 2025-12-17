package br.com.ifba.prg04_rodrigo_back_end.usuario.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Pessoa;
import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends PersistenceEntity {

    @Column(unique = true)
    private String email;
    private String senha;

    // Relacionamento com Pessoa
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    // Papéis do sistema
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Organizador organizador;

    // Futura entidade Turista
    // @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private Turista turista;

    public boolean isOrganizador() {
        return organizador != null;
    }
}
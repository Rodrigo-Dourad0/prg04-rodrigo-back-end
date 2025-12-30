package br.com.ifba.prg04_rodrigo_back_end.usuario.entity;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.PersistenceEntity;
import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Pessoa;
import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Usuario extends PersistenceEntity implements UserDetails {

    @Column(unique = true)
    private String email;
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id")
    @ToString.Exclude
    private Pessoa pessoa;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Organizador organizador;


    public boolean isOrganizador() {
        return organizador != null;
    }

    // --- MÉTODOS  DO USERDETAILS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (this.organizador != null) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ORGANIZADOR"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Controle de conta
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
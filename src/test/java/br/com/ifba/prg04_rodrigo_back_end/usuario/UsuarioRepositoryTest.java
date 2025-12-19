package br.com.ifba.prg04_rodrigo_back_end.usuario;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Pessoa;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para o Repositório de Usuário")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve encontrar usuário por email com sucesso")
    void findByEmail_whenSuccessful() {
        Usuario usuarioParaSalvar = this.createUsuario();
        this.usuarioRepository.save(usuarioParaSalvar);

        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByEmail("rodrigo@email.com");

        Assertions.assertThat(usuarioEncontrado.isPresent()).isTrue();
        Assertions.assertThat(usuarioEncontrado.get().getPessoa().getNome()).isEqualTo("Rodrigo");
        Assertions.assertThat(usuarioEncontrado.get().getEmail()).isEqualTo("rodrigo@email.com");
    }

    @Test
    @DisplayName("Não deve encontrar usuário quando o email não existe")
    void findByEmail_whenNotFound() {
        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByEmail("inexistente@email.com");

        Assertions.assertThat(usuarioEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve verificar se existe usuário por email")
    void existsByEmail_whenSuccessful() {
        Usuario usuario = this.createUsuario();
        this.usuarioRepository.save(usuario);

        boolean existe = this.usuarioRepository.existsByEmail("rodrigo@email.com");

        Assertions.assertThat(existe).isTrue();
    }

    private Usuario createUsuario() {
        Pessoa pessoa = Pessoa.builder()
                .nome("Rodrigo")
                .cpf("000.000.000-00")
                .telefone("71999999999")
                .build();

        return Usuario.builder()
                .email("rodrigo@email.com")
                .senha("123456")
                .pessoa(pessoa)
                .build();
    }
}
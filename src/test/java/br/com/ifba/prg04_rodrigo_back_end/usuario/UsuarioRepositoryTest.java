package br.com.ifba.prg04_rodrigo_back_end.usuario;

import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
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
        // CENÁRIO
        Usuario usuarioParaSalvar = this.createUsuario();
        this.usuarioRepository.save(usuarioParaSalvar);

        // AÇÃO
        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByEmail("rodrigo@email.com");

        // VERIFICAÇÃO
        Assertions.assertThat(usuarioEncontrado.isPresent()).isTrue(); // Tem que achar
        Assertions.assertThat(usuarioEncontrado.get().getNome()).isEqualTo("Rodrigo"); // O nome tem que bater
        Assertions.assertThat(usuarioEncontrado.get().getEmail()).isEqualTo("rodrigo@email.com"); // O email tem que bater
    }

    @Test
    @DisplayName("Não deve encontrar usuário quando o email não existe")
    void findByEmail_whenNotFound() {
        // AÇÃO
        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByEmail("inexistente@email.com");

        // VERIFICAÇÃO
        Assertions.assertThat(usuarioEncontrado).isEmpty();
    }


    @Test
    @DisplayName("Deve verificar se existe usuário por email")
    void existsByEmail_whenSuccessful() {
        // CENÁRIO
        Usuario usuario = this.createUsuario();
        this.usuarioRepository.save(usuario);

        // AÇÃO
        boolean existe = this.usuarioRepository.existsByEmail("rodrigo@email.com");

        // VERIFICAÇÃO
        Assertions.assertThat(existe).isTrue();
    }


    private Usuario createUsuario() {
        return Usuario.builder()
                .nome("Rodrigo")
                .email("rodrigo@email.com")
                .senha("123456")
                .cpf("000.000.000-00")
                .telefone("71999999999")
                .build();
    }

}

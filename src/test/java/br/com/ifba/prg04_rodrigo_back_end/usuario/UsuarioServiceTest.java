package br.com.ifba.prg04_rodrigo_back_end.usuario;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Pessoa;
import br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import br.com.ifba.prg04_rodrigo_back_end.usuario.service.UsuarioService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para Usuario Service")
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve salvar usuário com sucesso quando email não existir")
    void save_whenSuccessful() {
        Usuario usuario = createUsuario();

        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.save(usuario);

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getPessoa().getNome()).isEqualTo("Rodrigo");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar email duplicado")
    void save_throwsException_whenEmailExists() {
        Usuario usuario = createUsuario();

        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        Assertions.assertThatExceptionOfType(RegraDeNegocioException.class)
                .isThrownBy(() -> usuarioService.save(usuario))
                .withMessage("Já existe um usuário cadastrado com este e-mail.");

        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void findById_whenSuccessful() {
        Usuario usuario = createUsuario();
        Long id = 1L;

        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(id);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getEmail()).isEqualTo("rodrigo@email.com");
    }

    @Test
    @DisplayName("Deve lançar erro quando usuário não for encontrado pelo ID")
    void findById_throwsException_whenNotFound() {
        Long id = 99L;

        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(RegraDeNegocioException.class)
                .isThrownBy(() -> usuarioService.findById(id))
                .withMessage("Usuário não encontrado com id: " + id);
    }

    private Usuario createUsuario() {
        Pessoa pessoa = Pessoa.builder()
                .nome("Rodrigo")
                .build();

        Usuario usuario = Usuario.builder()
                .email("rodrigo@email.com")
                .senha("123456")
                .pessoa(pessoa)
                .build();

        usuario.setId(1L);

        return usuario;
    }
}
package br.com.ifba.prg04_rodrigo_back_end.usuario;

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

@ExtendWith(MockitoExtension.class) // 1. Habilita o uso de Mocks (Dublês)
@DisplayName("Testes para Usuario Service")
class UsuarioServiceTest {

    @InjectMocks // 2. Quem vai ser testado (O Mockito injeta os dublês aqui dentro)
    private UsuarioService usuarioService;

    @Mock // 3. O Dublê (O Repositório de mentirinha)
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve salvar usuário com sucesso quando email não existir")
    void save_whenSuccessful() {
        // --- CENÁRIO (Arrange) ---
        Usuario usuario = createUsuario();


        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);

        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // --- AÇÃO (Act) ---
        Usuario usuarioSalvo = usuarioService.save(usuario);

        // --- VERIFICAÇÃO (Assert) ---
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Rodrigo");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar email duplicado")
    void save_throwsException_whenEmailExists() {
        // --- CENÁRIO ---
        Usuario usuario = createUsuario();

        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        // --- AÇÃO & VERIFICAÇÃO ---

        Assertions.assertThatExceptionOfType(RegraDeNegocioException.class)
                .isThrownBy(() -> usuarioService.save(usuario))
                .withMessage("Já existe um usuário cadastrado com este e-mail.");


        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void findById_whenSuccessful() {
        // --- CENÁRIO ---
        Usuario usuario = createUsuario();
        Long id = 1L;


        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // --- AÇÃO ---
        Usuario resultado = usuarioService.findById(id);

        // --- VERIFICAÇÃO ---
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getEmail()).isEqualTo("rodrigo@email.com");
    }

    @Test
    @DisplayName("Deve lançar erro quando usuário não for encontrado pelo ID")
    void findById_throwsException_whenNotFound() {
        // --- CENÁRIO ---
        Long id = 99L;


        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // --- AÇÃO & VERIFICAÇÃO ---
        Assertions.assertThatExceptionOfType(RegraDeNegocioException.class)
                .isThrownBy(() -> usuarioService.findById(id))
                .withMessage("Usuário não encontrado com id: " + id);
    }

    // Método auxiliar para criar usuário
    private Usuario createUsuario() {

        Usuario usuario = Usuario.builder()
                .nome("Rodrigo")
                .email("rodrigo@email.com")
                .senha("123456")
                .build();

        usuario.setId(1L);

        return usuario;
    }
}
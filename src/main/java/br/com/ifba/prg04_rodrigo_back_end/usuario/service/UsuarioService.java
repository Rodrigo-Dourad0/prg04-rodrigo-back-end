package br.com.ifba.prg04_rodrigo_back_end.usuario.service;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Endereco;
import br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.UsuarioUpdateRequest;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {

        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }

        if (usuario.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(usuario);
    }

    @Override
    public Usuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com id: " + id));
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public void tornarOrganizador(Long usuarioId, Organizador organizador) {
        Usuario usuario = findById(usuarioId);

        if (usuario.isOrganizador()) {
            throw new RegraDeNegocioException("Este usuário já possui perfil de organizador.");
        }

        organizador.setUsuario(usuario);
        usuario.setOrganizador(organizador);

        repository.save(usuario);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Usuario usuario = findById(id);
        repository.delete(usuario);
    }

    @Override
    @Transactional
    public Usuario update(Long id, UsuarioUpdateRequest request) {
        // Busca o usuário existente
        Usuario usuarioExistente = findById(id);


        if (!usuarioExistente.getEmail().equals(request.email())) {
            repository.findByEmail(request.email()).ifPresent(u -> {

                if (!u.getId().equals(id)) {
                    throw new RegraDeNegocioException("Este e-mail já está em uso por outro usuário.");
                }
            });
            usuarioExistente.setEmail(request.email());
        }

        // Atualização dos dados de Pessoa
        if (usuarioExistente.getPessoa() != null) {
            // Atualiza telefone
            usuarioExistente.getPessoa().setTelefone(request.telefone());


            var end = usuarioExistente.getPessoa().getEndereco();


            if (end == null) {
                end = new Endereco();
                usuarioExistente.getPessoa().setEndereco(end);
            }

            // Mapeia cada campo individualmente para o banco de dados
            end.setRua(request.rua());
            end.setNumero(request.numero());
            end.setBairro(request.bairro());
            end.setCidade(request.cidade());
            end.setEstado(request.estado());
            end.setCep(request.cep());
        }

        // Persiste as alterações
        return repository.save(usuarioExistente);
    }
}